package org.openmrs.module.ethiohrireports.reports.datasetevaluator;

import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.ART_START_DATE;
import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.REASON_FOR_ART_ELIGIBILITY;
import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.TRANSFERRED_IN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ethiohrireports.reports.datasetdefinition.HtsNewDataSetDefinitionAdx;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.MapDataSet;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.evaluator.DataSetEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.HqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

@Handler(supports = { HtsNewDataSetDefinitionAdx.class })
public class HTsNewDataSetDefinitionEvaluatorAdx implements DataSetEvaluator {
	
	@Autowired
	EvaluationService evaluationService;
	
	@Autowired
	ConceptService conceptService;
	private HtsNewDataSetDefinitionAdx hdsd;
	private EvaluationContext context;
	private List<Obs> obses = new ArrayList<>();
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		
		 hdsd = (HtsNewDataSetDefinitionAdx) dataSetDefinition;
		context= evalContext;
		MapDataSet data = new MapDataSet(dataSetDefinition, evalContext);
	
		
		 obses =  getObservations();
		 
		 data.addData(new DataSetColumn("1_famale","<1,  Female, on Treatment",Integer.class)
		 ,getPersonCount(0, 1, Gender.Female));
		 data.addData(new DataSetColumn("1_male","<1,  Male, on Treatment",Integer.class)
		 ,getPersonCount(0, 1, Gender.Male));
 
		 data.addData(new DataSetColumn("1-4_famale","1-4, Female, on Treatment",Integer.class)
		 ,getPersonCount(1, 4, Gender.Female));
		 data.addData(new DataSetColumn("1-4_male","1-4,  Male, on Treatment",Integer.class)
		 ,getPersonCount(1, 4, Gender.Male));
		 
		 data.addData(new DataSetColumn("5-9_famale","5-9, Female, on Treatment",Integer.class)
		 ,getPersonCount(5, 9, Gender.Female));
		 data.addData(new DataSetColumn("5-9_male","5-9,  Male, on Treatment",Integer.class)
		 ,getPersonCount(5, 9, Gender.Male));
 
		 data.addData(new DataSetColumn("10-14_famale","10-14, Female, on Treatment",Integer.class)
		 ,getPersonCount(10, 14, Gender.Female));
		 data.addData(new DataSetColumn("10-14_male","10-14,  Male, on Treatment",Integer.class)
		 ,getPersonCount(10, 14, Gender.Male));
 
		 data.addData(new DataSetColumn("20-24_famale","20-24, Female, on Treatment",Integer.class)
		 ,getPersonCount(20, 24, Gender.Female));
		 data.addData(new DataSetColumn("20-24_male","20-24,  Male, on Treatment",Integer.class)
		 ,getPersonCount(20, 24, Gender.Male));
		 
		 data.addData(new DataSetColumn("25-29_famale","25-29, Female, on Treatment",Integer.class)
		 ,getPersonCount(25, 29, Gender.Female));
		 data.addData(new DataSetColumn("25-29_male","25-29,  Male, on Treatment",Integer.class)
		 ,getPersonCount(25, 29, Gender.Male));
 
 
		 data.addData(new DataSetColumn("30-34_famale","30-34, Female, on Treatment",Integer.class)
		 ,getPersonCount(30, 34, Gender.Female));
		 data.addData(new DataSetColumn("30-34_male","30-34,  Male, on Treatment",Integer.class)
		 ,getPersonCount(30, 34, Gender.Male));
 
		 data.addData(new DataSetColumn("35-39_famale","35-39, Female, on Treatment",Integer.class)
		 ,getPersonCount(35, 39, Gender.Female));
		 data.addData(new DataSetColumn("35-39_male","35-39,  Male, on Treatment",Integer.class)
		 ,getPersonCount(35, 39, Gender.Male));
 
		 data.addData(new DataSetColumn("40-44_famale","40-44, Female, on Treatment",Integer.class)
		 ,getPersonCount(40, 44, Gender.Female));
		 data.addData(new DataSetColumn("40-44_male","40-44,  Male, on Treatment",Integer.class)
		 ,getPersonCount(40, 44, Gender.Male));
 
		 data.addData(new DataSetColumn("45-49_famale","45-49, Female, on Treatment",Integer.class)
		 ,getPersonCount(45, 49, Gender.Female));
		 data.addData(new DataSetColumn("45-49_male","45-49,  Male, on Treatment",Integer.class)
		 ,getPersonCount(45, 49, Gender.Male));
 
		 data.addData(new DataSetColumn("50+_famale","50+, Female, on Treatment",Integer.class)
		 ,getPersonCount(50, 150, Gender.Female));
		 data.addData(new DataSetColumn("50+_male","50+,  Male, on Treatment",Integer.class)
		 ,getPersonCount(50, 150, Gender.Male));
		 return data;
		
	}
	
	private List<Obs> getObservations() {
      
        List<Obs> obses =new ArrayList<>();
       List<Integer> personIds = getPatientsWithARTStartedDate();
        HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obv")
        .from(Obs.class,"obv")
		.whereInAny("obv.concept",conceptService.getConceptByUuid(ART_START_DATE))
		.whereGreaterOrEqualTo("obv.valueDatetime", hdsd.getStartDate())
		.and()
		.whereEqual("obv.encounter.encounterType", hdsd.getEncounterType())
		.and()
		.whereLessOrEqualTo("obv.valueDatetime", hdsd.getEndDate())
		.and()
		.whereIn("obv.personId", personIds)
		.orderDesc("obv.personId,obv.obsDatetime");
        for (Object[] row : evaluationService.evaluateToList(queryBuilder, context)) {
			obses.add((Obs)row[0]);
		}

        return obses;
    }
	
	private List<Integer> getPatientsWithARTStartedDate(){
		List<Integer> uniqPatientsId = new ArrayList<>();

		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
      
        queryBuilder.select("obv.personId")
        .from(Obs.class,"obv")  
		.and()
		.whereEqual("obv.concept", conceptService.getConceptByUuid(REASON_FOR_ART_ELIGIBILITY)).and()
		.whereNotInAny("obv.valueCoded",Arrays.asList(conceptService.getConceptByUuid(TRANSFERRED_IN)))
		.and()
		.whereEqual("obv.encounter.encounterType", hdsd.getEncounterType()).and()
		  	
        .orderDesc("obv.personId,obv.obsDatetime") ;
		List<Integer> personIds = evaluationService.evaluateToList(queryBuilder, Integer.class, context);
		for (Integer personId : personIds) {
			if(!uniqPatientsId.contains(personId))
				uniqPatientsId.add(personId);
		}
		return uniqPatientsId;
	}
	
	private int getPersonCount(int minAge, int maxAge, Gender gender){
		int _age = 0;
		List<Integer> patients = new ArrayList<>();
			String _gender=gender.equals(Gender.Female)?"f":"m";
		for (Obs obs :obses) {
			_age =obs.getPerson().getAge();
			if (!patients.contains(obs.getPersonId()) 
			 && (_age>=minAge && _age< maxAge)
			 && (obs.getPerson().getGender().toLowerCase().equals(_gender))) {
				
				patients.add(obs.getPersonId());

			}
		}
		return patients.size();
	}
	
}
