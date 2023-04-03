package org.openmrs.module.ohrireports.reports.datasetevaluator.datim;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.ART_START_DATE;

import java.util.List;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.AutoCalculateDataSetDefinition;
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

@Handler(supports = { AutoCalculateDataSetDefinition.class })
public class AutoCalculateDataSetDefinitionEvaluator implements DataSetEvaluator {
	
	private EvaluationContext context;
	
	private AutoCalculateDataSetDefinition hdsd;
	
	private Concept artConcept;
	
	private String title = "Number of adults and children newly enrolled on antiretroviral therapy (ART)";
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private EvaluationService evaluationService;
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		
		hdsd = (AutoCalculateDataSetDefinition) dataSetDefinition;
		context = evalContext;
		artConcept = conceptService.getConceptByUuid(ART_START_DATE);
		
		DataSetRow dataSet = new DataSetRow();
		dataSet.addColumnValue(new DataSetColumn("adultAndChildrenEnrolled", "Numerator", Integer.class),
		    getTotalEnrolledPatients());
		SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);
		set.addRow(dataSet);
		return set;
	}
	
	private int getTotalEnrolledPatients() {
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("Distinct obs.personId").from(Obs.class, "obs")
		        .whereEqual("obs.encounter.encounterType", hdsd.getEncounterType()).and()
		        .whereEqual("obs.concept", artConcept).and().whereGreaterOrEqualTo("obs.valueDatetime", hdsd.getStartDate())
		        .and().whereLessOrEqualTo("obs.valueDatetime", hdsd.getEndDate()).orderDesc("obs.obsDatetime");
		
		List<Integer> personIDList = evaluationService.evaluateToList(queryBuilder, Integer.class, context);
		return personIDList.size();
	}
	
}
