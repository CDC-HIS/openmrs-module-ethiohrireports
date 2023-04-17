package org.openmrs.module.ohrireports.reports.datasetevaluator.datim.tb_art;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.ART_START_DATE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TB_DIAGNOSTIC_TEST_RESULT;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TREATMENT_END_DATE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TB_SCREENING_DATE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.POSITIVE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.tb_art.TBARTAutoCalculateDataSetDefinition;
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

@Handler(supports = { TBARTAutoCalculateDataSetDefinition.class })
public class TBARTAutoCalculateDataSetDefinitionEvaluator implements DataSetEvaluator {

    private EvaluationContext context;
    private int total = 0;
    private int minCount = 0;
    private int maxCount = 4;
    List<Obs> obses = new ArrayList<>();
    private TBARTAutoCalculateDataSetDefinition hdsd;

    private Concept artConcept, treatmentConcept, tbScreenDateConcept, tbDiagnosticTestResultConcept, positiveConcept;

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private EvaluationService evaluationService;

    @Override
    public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext)
            throws EvaluationException {

        hdsd = (TBARTAutoCalculateDataSetDefinition) dataSetDefinition;
        context = evalContext;
        setRequiredConcepts();

        DataSetRow dataSet = new DataSetRow();
        dataSet.addColumnValue(new DataSetColumn("auto-calculate","Numerator", Integer.class), 
        getTotalCount());
        SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);
        set.addRow(dataSet);
        return set;
    }

    private void setRequiredConcepts() {
        artConcept = conceptService.getConceptByUuid(ART_START_DATE);
        treatmentConcept = conceptService.getConceptByUuid(TREATMENT_END_DATE);
        tbScreenDateConcept = conceptService.getConceptByUuid(TB_SCREENING_DATE);
        tbDiagnosticTestResultConcept = conceptService.getConceptByUuid(TB_DIAGNOSTIC_TEST_RESULT);
        positiveConcept = conceptService.getConceptByUuid(POSITIVE);
    }

    private void setObservations() {
        HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
        queryBuilder.select("obs").from(Obs.class, "obs")
                .whereEqual("obs.encounter.encounterType", hdsd.getEncounterType())
                .and()
                .whereEqual("obs.concept", artConcept)
                .and()
                .whereBetweenInclusive("obs.valueDatetime", hdsd.getStartDate(), hdsd.getEndDate())
                .and()
                .whereIdIn("obs.personId", getPatientsWithTB());

        obses = evaluationService.evaluateToList(queryBuilder, Obs.class, context);

    }

    private int getTotalCount() {
        setObservations();
        List<Integer> personIds = new ArrayList<>();
        for (Obs obs : obses) {
            if (personIds.contains(obs.getPersonId()))
                continue;
            personIds.add(obs.getPersonId());

        }
        return personIds.size();
    }

    private List<Integer> getOnTreatmentPatients() {
        HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
        queryBuilder.select("distinct obs.personId").from(Obs.class, "obs")
                .whereEqual("obs.encounter.encounterType", hdsd.getEncounterType()).and()
                .whereEqual("obs.concept", treatmentConcept).and()
                .whereGreater("obs.valueDatetime", hdsd.getStartDate());
        return evaluationService.evaluateToList(queryBuilder, Integer.class, context);
    }

    private List<Integer> getPatientsWithTB() {
        HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
        queryBuilder.select("distinct obs.personId").from(Obs.class, "obs")
                .whereEqual("obs.encounter.encounterType", hdsd.getEncounterType())
                .and().whereEqual("obs.concept", tbDiagnosticTestResultConcept).and()
                .whereEqual("obs.valueCoded", positiveConcept)
                .and().whereIdIn("obs.personId", getOnTreatmentPatients());
        return evaluationService.evaluateToList(queryBuilder, Integer.class, context);

    }
}
