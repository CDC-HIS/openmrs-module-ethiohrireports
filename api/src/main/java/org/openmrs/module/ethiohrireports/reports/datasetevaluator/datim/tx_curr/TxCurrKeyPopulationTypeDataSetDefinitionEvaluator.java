package org.openmrs.module.ethiohrireports.reports.datasetevaluator.datim.tx_curr;



import java.util.ArrayList;


import java.util.List;
import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ethiohrireports.reports.datasetdefinition.datim.tx_curr.TxCurrKeyPopulationTypeDataSetDefinition;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.dataset.SimpleDataSet;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.evaluator.DataSetEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

@Handler(supports = { TxCurrKeyPopulationTypeDataSetDefinition.class })
public class TxCurrKeyPopulationTypeDataSetDefinitionEvaluator implements DataSetEvaluator {
	
	private EvaluationContext context;
	
	private TxCurrKeyPopulationTypeDataSetDefinition hdsd;
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private EvaluationService evaluationService;

    List<Obs> obses = new ArrayList<>();
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		
		hdsd = (TxCurrKeyPopulationTypeDataSetDefinition) dataSetDefinition;
		context = evalContext;
        SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);
		// total=0;
        DataSetRow keyPupulation = new DataSetRow();
        keyPupulation.addColumnValue(new DataSetColumn("keyPopulation", "key Population Type",Integer.class), "PWID");
        keyPupulation.addColumnValue(new DataSetColumn("value", "value", Integer.class),0);
        set.addRow(keyPupulation);
		keyPupulation = new DataSetRow();
		keyPupulation.addColumnValue(new DataSetColumn("keyPopulation", "key Population Type",Integer.class), "MSM");
        keyPupulation.addColumnValue(new DataSetColumn("value", "value", Integer.class),0);
		set.addRow(keyPupulation);
		keyPupulation = new DataSetRow();
		keyPupulation.addColumnValue(new DataSetColumn("keyPopulation", "key Population Type",Integer.class), "Transgender people");
        keyPupulation.addColumnValue(new DataSetColumn("value", "value", Integer.class),0);
		set.addRow(keyPupulation);
		keyPupulation = new DataSetRow();
		keyPupulation.addColumnValue(new DataSetColumn("keyPopulation", "key Population Type",Integer.class), "FSM");
        keyPupulation.addColumnValue(new DataSetColumn("value", "value", Integer.class),0);
		set.addRow(keyPupulation);
		keyPupulation = new DataSetRow();
		keyPupulation.addColumnValue(new DataSetColumn("keyPopulation", "key Population Type",Integer.class), "People in prison and other closed setting");
        keyPupulation.addColumnValue(new DataSetColumn("value", "value", Integer.class),0);
		set.addRow(keyPupulation); 
        return set;
	}
	}
