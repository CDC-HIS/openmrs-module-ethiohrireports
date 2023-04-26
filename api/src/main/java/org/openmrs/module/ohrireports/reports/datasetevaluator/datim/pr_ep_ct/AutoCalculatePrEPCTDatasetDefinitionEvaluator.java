package org.openmrs.module.ohrireports.reports.datasetevaluator.datim.pr_ep_ct;

import org.openmrs.Concept;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.pr_ep_ct.AutoCalculatePrEPCTDatasetDefinition;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.SimpleDataSet;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.evaluator.DataSetEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

@Handler(supports = { AutoCalculatePrEPCTDatasetDefinition.class })
public class AutoCalculatePrEPCTDatasetDefinitionEvaluator implements DataSetEvaluator {
	
	private Concept artConcept, treatmentConcept, tbScreenDateConcept, tbDiagnosticTestResultConcept, positiveConcept;
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private EvaluationService evaluationService;
	
	private AutoCalculatePrEPCTDatasetDefinition aucDataset;
	
	private EvaluationContext context;
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		
		aucDataset = (AutoCalculatePrEPCTDatasetDefinition) dataSetDefinition;
		context = evalContext;
		SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);
		
		return set;
	}
	
}
