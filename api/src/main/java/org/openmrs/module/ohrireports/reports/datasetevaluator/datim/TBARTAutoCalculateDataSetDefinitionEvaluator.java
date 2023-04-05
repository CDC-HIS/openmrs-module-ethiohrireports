package org.openmrs.module.ohrireports.reports.datasetevaluator.datim;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.ART_START_DATE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TB_DIAGNOSTIC_TEST_RESULT;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TREATMENT_END_DATE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TB_SCREENING_DATE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.POSITIVE;

import java.util.List;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.TBARTAutoCalculateDataSetDefinition;
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
	
	private TBARTAutoCalculateDataSetDefinition hdsd;
	
	private Concept artConcept,treatmentConcept,tbScreenDateConcept,
	tbDiagnosticTestResultConcept,positiveConcept;
	
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private EvaluationService evaluationService;
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		
		hdsd = (TBARTAutoCalculateDataSetDefinition) dataSetDefinition;
		context = evalContext;
		setRequiredConcepts();
		
		DataSetRow dataSet = new DataSetRow();
		dataSet.addColumnValue(new DataSetColumn("adultAndChildrenEnrolled", "Numerator", Integer.class),
		    getTotalEnrolledPatients());
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

	private List<Obs> getTotalPatients(String gender) {
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs").from(Obs.class, "obs")
					.whereEqual("obs.encounter.encounterType", hdsd.getEncounterType())
					.and()
					.whereEqual("obs.concept", artConcept)
					.and();
				if(hdsd.getIsNewlyEnrolled())
				{
					queryBuilder.whereBetweenInclusive("obs.valueDatetime", hdsd.getStartDate(), hdsd.getEndDate())

				}else{
					queryBuilder.whereLess("obs.valueDatetime", hdsd.getStartDate());
				}
			
		queryBuilder.whereIdIn("obs.personId", getPatientsWithTB());

		List<Obs> obses = evaluationService.evaluateToList(queryBuilder, Obs.class, context);

		return obses;
	}

	
	private List<Integer> getOnTreatmentPatients(){
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("distinct obs.personId")
					.from(Obs.class,"obs")
					.whereEqual("obs.encounter.encounterType", hdsd.getEncounterType())
					.and()
					.whereEqual("obs.concept", TREATMENT_END_DATE)
					.and()
					.whereGreater("obs.valueDatetime", hdsd.getStartDate());
				return evaluationService.evaluateToList(queryBuilder, Integer.class, context);
	}

	private List<Integer> getPatientsWithTB(){
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs")
					.from(Obs.class,"obs")
					.whereEqual("obs.encounter.encounterType", hdsd.getEncounterType())
					.and()
					.whereEqual("obs.concept", TB_DIAGNOSTIC_TEST_RESULT)
					.and()
					.whereEqual("obs.valueCoded",POSITIVE)
					.and()
					.whereIdIn("obs.personId", getOnTreatmentPatients());
			return evaluationService.evaluateToList(queryBuilder, Integer.class, context);

	}
}
