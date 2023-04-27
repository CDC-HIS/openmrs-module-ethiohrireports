package org.openmrs.module.ohrireports.reports.datasetevaluator.datim.pr_ep_ct;

import org.openmrs.annotation.Handler;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.pr_ep_ct.PrEPCTByPopulationTypeDatasetDefinition;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.dataset.SimpleDataSet;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.evaluator.DataSetEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

@Handler(supports = { PrEPCTByPopulationTypeDatasetDefinition.class })
public class PrEPCTByPopulationTypeDatasetDefinitionEvaluator implements DataSetEvaluator {
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		
		SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);
		DataSetRow dRow = new DataSetRow();
		dRow.addColumnValue(new DataSetColumn("Name", "Name", String.class), "PWID");
		dRow.addColumnValue(new DataSetColumn("-", "", Integer.class), 0);
		set.addRow(dRow);
		
		DataSetRow msmDataSetRow = new DataSetRow();
		msmDataSetRow.addColumnValue(new DataSetColumn("Name", "Name", String.class), "MSM");
		msmDataSetRow.addColumnValue(new DataSetColumn("-", "", Integer.class), 0);
		set.addRow(msmDataSetRow);
		
		DataSetRow tgpe = new DataSetRow();
		tgpe.addColumnValue(new DataSetColumn("Name", "Name", String.class), "Transgender People");
		tgpe.addColumnValue(new DataSetColumn("-", "", Integer.class), 0);
		set.addRow(tgpe);
		
		DataSetRow fSetRow = new DataSetRow();
		fSetRow.addColumnValue(new DataSetColumn("Name", "Name", String.class), "FSW");
		fSetRow.addColumnValue(new DataSetColumn("-", "-", Integer.class), 0);
		set.addRow(fSetRow);
		
		DataSetRow pipDataSetRow = new DataSetRow();
		pipDataSetRow.addColumnValue(new DataSetColumn("Name", "Name", String.class),
		    "People in prison and other closed settings");
		pipDataSetRow.addColumnValue(new DataSetColumn("-", "", Integer.class), 0);
		set.addRow(pipDataSetRow);
		return set;
		
	}
	
}
