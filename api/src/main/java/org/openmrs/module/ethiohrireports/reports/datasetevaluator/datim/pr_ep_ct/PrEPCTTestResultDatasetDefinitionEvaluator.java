package org.openmrs.module.ethiohrireports.reports.datasetevaluator.datim.pr_ep_ct;

import org.openmrs.annotation.Handler;
import org.openmrs.module.ethiohrireports.reports.datasetdefinition.datim.pr_ep_ct.PrEPCTTestResultDatasetDefinition;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.dataset.SimpleDataSet;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.evaluator.DataSetEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

@Handler(supports = { PrEPCTTestResultDatasetDefinition.class })
public class PrEPCTTestResultDatasetDefinitionEvaluator implements DataSetEvaluator {
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		
		SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);
		DataSetRow dRow = new DataSetRow();
		dRow.addColumnValue(new DataSetColumn("Name", "Name", String.class), "Positive");
		dRow.addColumnValue(new DataSetColumn("-", "", Integer.class), 0);
		set.addRow(dRow);
		
		DataSetRow msmDataSetRow = new DataSetRow();
		msmDataSetRow.addColumnValue(new DataSetColumn("Name", "Name", String.class), "Negative");
		msmDataSetRow.addColumnValue(new DataSetColumn("-", "", Integer.class), 0);
		set.addRow(msmDataSetRow);
		
		DataSetRow tgpe = new DataSetRow();
		tgpe.addColumnValue(new DataSetColumn("Name", "Name", String.class), "Other");
		tgpe.addColumnValue(new DataSetColumn("-", "", Integer.class), 0);
		set.addRow(tgpe);
		
		return set;
		
	}
	
}
