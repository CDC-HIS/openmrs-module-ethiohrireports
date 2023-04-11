package org.openmrs.module.ohrireports.reports.datasetevaluator.datim;

import org.openmrs.annotation.Handler;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.PopulationTypeDataSetDefinition;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.dataset.SimpleDataSet;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.evaluator.DataSetEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

@Handler(supports = { PopulationTypeDataSetDefinition.class })
public class PopulationTypeDataSetDefinitionEvaluator implements DataSetEvaluator {
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		
		SimpleDataSet dataSet = new SimpleDataSet(dataSetDefinition, evalContext);
		
		DataSetRow PWIDRow = new DataSetRow();
		PWIDRow.addColumnValue(new DataSetColumn("PopulationType", "Population Type", String.class), "PWID");
		PWIDRow.addColumnValue(new DataSetColumn("total", "Total", Integer.class), 0);
		dataSet.addRow(PWIDRow);
		
		DataSetRow mSMRow = new DataSetRow();
		mSMRow.addColumnValue(new DataSetColumn("PopulationType", "Population Type", String.class), "MSM");
		mSMRow.addColumnValue(new DataSetColumn("total", "Total", Integer.class), 0);
		dataSet.addRow(mSMRow);
		
		DataSetRow transGRow = new DataSetRow();
		transGRow.addColumnValue(new DataSetColumn("PopulationType", "Population Type", String.class), "Transgender People");
		transGRow.addColumnValue(new DataSetColumn("total", "Total", Integer.class), 0);
		dataSet.addRow(transGRow);
		
		DataSetRow fSWRow = new DataSetRow();
		fSWRow.addColumnValue(new DataSetColumn("PopulationType", "Population Type", String.class), "FSW");
		fSWRow.addColumnValue(new DataSetColumn("total", "Total", Integer.class), 0);
		dataSet.addRow(fSWRow);
		
		DataSetRow pPCRow = new DataSetRow();
		pPCRow.addColumnValue(new DataSetColumn("PopulationType", "Population Type", String.class),
		    "People in prison and other closed settings");
		pPCRow.addColumnValue(new DataSetColumn("total", "Total", Integer.class), 0);
		dataSet.addRow(pPCRow);
		
		return dataSet;
	}
	
}
