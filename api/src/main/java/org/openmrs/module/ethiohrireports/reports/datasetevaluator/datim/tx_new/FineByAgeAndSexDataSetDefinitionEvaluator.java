package org.openmrs.module.ethiohrireports.reports.datasetevaluator.datim.tx_new;

import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.ART_START_DATE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ethiohrireports.reports.datasetdefinition.datim.tx_new.FineByAgeAndSexDataSetDefinition;
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

@Handler(supports = { FineByAgeAndSexDataSetDefinition.class })
public class FineByAgeAndSexDataSetDefinitionEvaluator implements DataSetEvaluator {

    private EvaluationContext context;

    private FineByAgeAndSexDataSetDefinition hdsd;
    private Concept artConcept;
    private int total = 0;
    @Autowired
    private ConceptService conceptService;

    @Autowired
    private EvaluationService evaluationService;
    private int minCount = 0;
    private int maxCount = 4;
    List<Obs> obses = new ArrayList<>();

    @Override
    public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext)
            throws EvaluationException {
        total = 0;
        hdsd = (FineByAgeAndSexDataSetDefinition) dataSetDefinition;
        context = evalContext;
        artConcept = conceptService.getConceptByUuid(ART_START_DATE);
        SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);

        // Female aggregation
        obses = LoadObs("F");
        DataSetRow femaleDateSet = new DataSetRow();
        buildDataSet(femaleDateSet, "F");
        set.addRow(femaleDateSet);

        // Male aggregation
        obses = LoadObs("M");
        DataSetRow maleDataSet = new DataSetRow();
        buildDataSet(maleDataSet, "M");
        set.addRow(maleDataSet);
        return set;
    }

    private void buildDataSet(DataSetRow dataSet, String gender) {
        total = 0;
        minCount = 1;
        maxCount = 4;
        dataSet.addColumnValue(new DataSetColumn("FineByAgeAndSexData", "Gender",
                Integer.class), gender.equals("F") ? "Female" : "Male");
        dataSet.addColumnValue(new DataSetColumn("unknownAge", "Unknown Age", Integer.class),
                getEnrolledByUnknownAge());

        dataSet.addColumnValue(new DataSetColumn("<1", "Below One (<1)", Integer.class),
                getEnrolledBelowOneYear());

        while (minCount <= 65) {
            if (minCount == 65) {
                dataSet.addColumnValue(new DataSetColumn("65+", "65", Integer.class),
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

    private List<Obs> LoadObs(String gender) {
        HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
        queryBuilder.select("obs")
                .from(Obs.class, "obs")
                .whereEqual("obs.encounter.encounterType", hdsd.getEncounterType())
                .and()
                .whereEqual("obs.person.gender", gender)
                .and()
                .whereEqual("obs.concept", artConcept)
                .and()
                .whereGreaterOrEqualTo("obs.valueDatetime", hdsd.getStartDate())
                .and()
                .whereLessOrEqualTo("obs.valueDatetime", hdsd.getEndDate())
                .orderDesc("obs.obsDatetime");

        List<Obs> obses = evaluationService.evaluateToList(queryBuilder, Obs.class, context);
        return obses;
    }

}
