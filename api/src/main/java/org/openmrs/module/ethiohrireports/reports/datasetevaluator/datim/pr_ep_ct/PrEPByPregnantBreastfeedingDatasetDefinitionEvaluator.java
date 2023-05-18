package org.openmrs.module.ethiohrireports.reports.datasetevaluator.datim.pr_ep_ct;

import org.openmrs.annotation.Handler;
import org.openmrs.module.ethiohrireports.reports.datasetdefinition.datim.pr_ep_ct.PrEPCTPregnantBreastfeedingDatasetDefinition;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.dataset.SimpleDataSet;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.evaluator.DataSetEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

@Handler(supports = { PrEPCTPregnantBreastfeedingDatasetDefinition.class })
public class PrEPByPregnantBreastfeedingDatasetDefinitionEvaluator implements DataSetEvaluator {
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		
		SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);
		DataSetRow dRow = new DataSetRow();
		dRow.addColumnValue(new DataSetColumn("Name", "Name", String.class), "Pregnant");
		dRow.addColumnValue(new DataSetColumn("-", "", Integer.class), 0);
		set.addRow(dRow);
		
		DataSetRow msmDataSetRow = new DataSetRow();
		msmDataSetRow.addColumnValue(new DataSetColumn("Name", "Name", String.class), "Breastfeeding");
		msmDataSetRow.addColumnValue(new DataSetColumn("-", "", Integer.class), 0);
		set.addRow(msmDataSetRow);
		
		return set;
		
	}
	
}
