package org.openmrs.module.ethiohrireports.reports.datasetevaluator.datim.pmtct_art;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.ART_START_DATE;
import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.TB_DIAGNOSTIC_TEST_RESULT;
import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.TREATMENT_END_DATE;
import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.TB_SCREENING_DATE;
import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.POSITIVE;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ethiohrireports.reports.datasetdefinition.datim.pmtct_art.PMTCTARTDataSetDefinition;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.dataset.SimpleDataSet;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.evaluator.DataSetEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.HqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

@Handler(supports = { PMTCTARTDataSetDefinition.class })
public class PMTCTARTDataSetDefinitionEvaluator implements DataSetEvaluator {

    private EvaluationContext context;
    private int total = 0;
    private int minCount = 0;
    private int maxCount = 4;
    List<Obs> obses = new ArrayList<>();
    private PMTCTARTDataSetDefinition hdsd;

    private Concept artConcept, treatmentConcept, tbScreenDateConcept, tbDiagnosticTestResultConcept, positiveConcept;

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private EvaluationService evaluationService;

    @Override
    public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext)
            throws EvaluationException {
        hdsd = (PMTCTARTDataSetDefinition) dataSetDefinition;
        context = evalContext;
        setRequiredConcepts();
        SimpleDataSet simpleDataSet = new SimpleDataSet(dataSetDefinition, evalContext);
        buildDataSet(simpleDataSet, true);

        return simpleDataSet;

    }

    private void buildDataSet(SimpleDataSet simpleDataSet, boolean isAlreadyOnArt) {
        setObservations("F",isAlreadyOnArt);
        DataSetRow femaleSetRow = new DataSetRow();
        buildDataSet(femaleSetRow, "F");
        simpleDataSet.addRow(femaleSetRow);

    }

    private void buildDataSet(DataSetRow dataSet, String gender) {
        total = 0;
        minCount = 1;
        maxCount = 4;

        dataSet.addColumnValue(new DataSetColumn("ByAgeAndSexData", "Gender",
                Integer.class), gender.equals("F") ? "Female" : "Male");
        dataSet.addColumnValue(new DataSetColumn("unknownAge", "Unknown Age", Integer.class),
                getEnrolledByUnknownAge());

        dataSet.addColumnValue(new DataSetColumn("<1", "Below One (<1)", Integer.class),
                getEnrolledBelowOneYear());

        while (minCount <= 65) {
            if (minCount == 65) {
                dataSet.addColumnValue(new DataSetColumn("65+", "65+", Integer.class),
                        getEnrolledByAgeAndGender(65, 200, gender));
            } else {
                dataSet.addColumnValue(
                        new DataSetColumn(minCount + "-" + maxCount, minCount + "-" + maxCount, Integer.class),
                        getEnrolledByAgeAndGender(minCount, maxCount, gender));
            }
            minCount = maxCount + 1;
            maxCount = minCount + 4;
        }
        dataSet.addColumnValue(new DataSetColumn("Sub-total", "Subtotal", Integer.class),
                total);
    }

    private int getEnrolledByAgeAndGender(int min, int max, String gender) {
        int count = 0;
        List<Integer> personIds = new ArrayList<>();
        for (Obs obs : obses) {

            if (personIds.contains(obs.getPersonId()))
                continue;

            if (obs.getPerson().getAge() >= min && obs.getPerson().getAge() <= max) {
                personIds.add(obs.getPersonId());
                count++;
            }
        }
        incrementTotalCount(count);
        clearCountedPerson(personIds);
        return count;
    }

    private int getEnrolledByUnknownAge() {
        int count = 0;
        List<Integer> personIds = new ArrayList<>();
        for (Obs obs : obses) {
            if (personIds.contains(obs.getPersonId()))
                continue;

            if (Objects.isNull(obs.getPerson().getAge()) ||
                    obs.getPerson().getAge() <= 0) {
                count++;
                personIds.add(obs.getPersonId());
            }

        }
        incrementTotalCount(count);
        clearCountedPerson(personIds);
        return count;
    }

    private int getEnrolledBelowOneYear() {
        int count = 0;
        List<Integer> personIds = new ArrayList<>();
        for (Obs obs : obses) {
            if (personIds.contains(obs.getPersonId()))
                continue;

            if ((obs.getPerson().getAge() < 1)) {
                count++;
                personIds.add(obs.getPersonId());
            }
        }
        incrementTotalCount(count);
        clearCountedPerson(personIds);
        return count;
    }

    private void incrementTotalCount(int count) {
        if (count > 0)
            total = total + count;
    }

    private void clearCountedPerson(List<Integer> personIds) {
        for (int pId : personIds) {
            obses.removeIf(p -> p.getPersonId().equals(pId));
        }
    }

    private void setRequiredConcepts() {
        artConcept = conceptService.getConceptByUuid(ART_START_DATE);
        treatmentConcept = conceptService.getConceptByUuid(TREATMENT_END_DATE);
        tbScreenDateConcept = conceptService.getConceptByUuid(TB_SCREENING_DATE);
        tbDiagnosticTestResultConcept = conceptService.getConceptByUuid(TB_DIAGNOSTIC_TEST_RESULT);
        positiveConcept = conceptService.getConceptByUuid(POSITIVE);
    }

    private void setObservations(String gender,boolean isAlreadyOnArt) {
        HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
        queryBuilder.select("obs").from(Obs.class, "obs")
                .whereEqual("obs.encounter.encounterType", hdsd.getEncounterType())
                .and()
                .whereEqual("obs.person.gender", gender)
                .whereEqual("obs.concept", artConcept).and();
            if (!isAlreadyOnArt) {
            queryBuilder.whereBetweenInclusive("obs.valueDatetime", hdsd.getStartDate(),
             hdsd.getEndDate());

            }else {
                queryBuilder.whereLess("obs.valueDatetime", hdsd.getStartDate());
            }

        queryBuilder.whereIdIn("obs.personId", getPregnantPatients());

        obses = evaluationService.evaluateToList(queryBuilder, Obs.class, context);

    }

    private List<Integer> getOnTreatmentPatients() {
        HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
        queryBuilder.select("distinct obs.personId").from(Obs.class, "obs")
                .whereEqual("obs.encounter.encounterType", hdsd.getEncounterType()).and()
                .whereEqual("obs.concept", treatmentConcept).and()
                .whereGreater("obs.valueDatetime", hdsd.getStartDate());
        return evaluationService.evaluateToList(queryBuilder, Integer.class, context);
    }
    

    private List<Integer> getPregnantPatients() {
        //TODO: update the query for the  pregnant patients
        HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
        queryBuilder.select("obs").from(Obs.class, "obs")
                .whereEqual("obs.encounter.encounterType", hdsd.getEncounterType())
                .and().whereEqual("obs.concept", tbDiagnosticTestResultConcept).and()
                .whereEqual("obs.valueCoded", positiveConcept)
                .and().whereIdIn("obs.personId", getOnTreatmentPatients());
        return evaluationService.evaluateToList(queryBuilder, Integer.class, context);

    }
}
