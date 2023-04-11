package org.openmrs.module.ohrireports.reports.datasetevaluator.datim;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.ART_START_DATE;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.DatimTxNewDataSetDefinition;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.dataset.MapDataSet;
import org.openmrs.module.reporting.dataset.SimpleDataSet;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.evaluator.DataSetEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.HqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

@Handler(supports = { DatimTxNewDataSetDefinition.class })
public class DatimTxNewDataSetDefinitionEvaluator implements DataSetEvaluator {
	
	private EvaluationContext context;
	
	private DatimTxNewDataSetDefinition hdsd;
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private EvaluationService evaluationService;
	
	private Concept artConcept;
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		
		hdsd = (DatimTxNewDataSetDefinition) dataSetDefinition;
		context = evalContext;
		artConcept = conceptService.getConceptByUuid(ART_START_DATE);
		
		SimpleDataSet dataSet = new SimpleDataSet(dataSetDefinition, evalContext);
		
		// dataSet.addRow(new DataSetRow().addColumnValue(new DataSetColumn("adult-number", "(Auto-Calculate) Number Of Adults and children newly Enrolled on antiretroviral therapy(ART)", String.class),""));
		return dataSet;
		// MapDataSet data = new MapDataSet(dataSetDefinition, evalContext);
		// data.addData(new DataSetColumn("number_of_children_on_art",
		//         "(Auto-Calculate) Number Of Adults and children newly Enrolled on antiretroviral therapy(ART)",
		//         Integer.class), getNewlyEnrolledPatients(-1, -1, " "));
		
		// data.addData(new DataSetColumn("female", "Female ", String.class), "");
		// buildDataSet(data, "F");
		// data.addData(new DataSetColumn("Male", "Male ", String.class), "");
		// buildDataSet(data, "M");
		// return data;
		
	}
	
	private void buildDataSet(MapDataSet dataSet, String gender) {
		dataSet.addData(new DataSetColumn("unknown", "Unknown", Integer.class), getNewlyEnrolledPatients(0, 0, gender));
		dataSet.addData(new DataSetColumn("above", "<1 ", Integer.class), getNewlyEnrolledPatients(0, 1, gender));
		for (int i = 1; i <= 50; i = i + 4) {
			
			if (i >= 46) {
				dataSet.addData(new DataSetColumn("50+", "50+", Integer.class), getNewlyEnrolledPatients(i, i, gender));
			} else if (i == 1) {
				dataSet.addData(new DataSetColumn("1-4", "1-4", Integer.class), getNewlyEnrolledPatients(i, 4, gender));
			} else {
				dataSet.addData(new DataSetColumn(i + "-" + i + 4, i + "-" + i + 4, Integer.class),
				    getNewlyEnrolledPatients(i, i + 4, gender));
			}
			
		}
		
	}
	
	private Integer getNewlyEnrolledPatients(int minAge, int maxAge, String gender) {
        HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
        queryBuilder.select("obs").from(Obs.class, "obs")
		.whereEqual("obs.concept", artConcept);
      
        if (!gender.equals(" "))
            queryBuilder.and().whereEqual("obs.person.gender", gender);
     
     
            queryBuilder.and()
                .whereEqual("obs.encounter.encounterType", hdsd.getEncounterType()).and()
                .whereGreaterOrEqualTo("obs.valueDatetime", hdsd.getStartDate())
                .whereLessOrEqualTo("obs.valueDatetime", hdsd.getEndDate())
                .orderDesc("obs.obsDatetime");

        List<Obs> obses = evaluationService.evaluateToList(queryBuilder, Obs.class, context);
        List<Integer> persoIntegers = new ArrayList<>();
        int count = 0;
        for (Obs obs : obses) {
            if (persoIntegers.contains(obs.getPersonId()))
                continue;
			if(minAge<0 && maxAge<0){
				count++;
			} else 
            if (minAge >= 0 && maxAge <= 1 && obs.getPerson().getAge() <= 1) {
                count++;
            } else if (obs.getPerson().getAge() >= minAge && obs.getPerson().getAge() <= maxAge) {
                count++;
            }

            persoIntegers.add(obs.getPersonId());
        }

        return count;
    }
}
