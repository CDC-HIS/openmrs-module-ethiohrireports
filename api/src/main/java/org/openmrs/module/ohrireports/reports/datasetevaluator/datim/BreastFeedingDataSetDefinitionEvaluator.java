package org.openmrs.module.ohrireports.reports.datasetevaluator.datim;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.ART_START_DATE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.CURRENTLY_BREAST_FEEDING_CHILD;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.YES;

import java.util.List;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.BreastFeedingStatusDataSetDefinition;
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

@Handler(supports = { BreastFeedingStatusDataSetDefinition.class })
public class BreastFeedingDataSetDefinitionEvaluator implements DataSetEvaluator {
	
	private EvaluationContext context;
	
	private BreastFeedingStatusDataSetDefinition hdsd;
	
	private Concept artConcept, breastFeeding, breastFeedingYes;
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private EvaluationService evaluationService;
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		
		hdsd = (BreastFeedingStatusDataSetDefinition) dataSetDefinition;
		context = evalContext;
		artConcept = conceptService.getConceptByUuid(ART_START_DATE);
		breastFeeding = conceptService.getConceptByUuid(CURRENTLY_BREAST_FEEDING_CHILD);
		breastFeedingYes = conceptService.getConceptByUuid(YES);
		DataSetRow dataSet = new DataSetRow();
		dataSet.addColumnValue(new DataSetColumn("breastFeeding", "BreastFeeding", Integer.class),
		    getNumberOfEnrolledBreastFeeding());
		SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);
		set.addRow(dataSet);
		return set;
	}
	
	private List<Integer> getTotalEnrolledFemalePatients() {
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("Distinct obs.personId").from(Obs.class, "obs")
		        .whereEqual("obs.encounter.encounterType", hdsd.getEncounterType()).and()
		        .whereEqual("obs.person.gender", "F").and().whereEqual("obs.concept", artConcept).and()
		        .whereGreaterOrEqualTo("obs.valueDatetime", hdsd.getStartDate()).and()
		        .whereLessOrEqualTo("obs.valueDatetime", hdsd.getEndDate());
		
		List<Integer> personIDList = evaluationService.evaluateToList(queryBuilder, Integer.class, context);
		return personIDList;
	}
	
	public int getNumberOfEnrolledBreastFeeding() {
		List<Integer> pList = getTotalEnrolledFemalePatients();
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("distinct obs.personId").from(Obs.class, "obs").whereEqual("obs.concept", breastFeeding).and()
		        .whereEqual("obs.valueCoded", breastFeedingYes).and().whereIn("obs.personId", pList);
		List<Integer> personIDs = evaluationService.evaluateToList(queryBuilder, Integer.class, context);
		return personIDs.size();
		
	}
}
