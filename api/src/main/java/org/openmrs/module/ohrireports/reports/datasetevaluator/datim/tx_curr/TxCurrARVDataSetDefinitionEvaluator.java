package org.openmrs.module.ohrireports.reports.datasetevaluator.datim.tx_curr;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.ALIVE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.PATIENT_STATUS;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.RESTART;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TREATMENT_END_DATE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.ARV_DISPENSED_IN_DAYS;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;
import java.util.Objects;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.tx_curr.TxCurrARVDataSetDefinition;
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

@Handler(supports = { TxCurrARVDataSetDefinition.class })
public class TxCurrARVDataSetDefinitionEvaluator implements DataSetEvaluator {
	
	private EvaluationContext context;
	
	private TxCurrARVDataSetDefinition hdsd;
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private EvaluationService evaluationService;
    private int minCount = 0;
    private int maxCount = 4;
	// private int total = 0;
    List<Obs> obses = new ArrayList<>();
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		
		hdsd = (TxCurrARVDataSetDefinition) dataSetDefinition;
		context = evalContext;
        SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);
		// total=0;
        DataSetRow femaleDateSet = new DataSetRow();
        obses = getARVDispenced("F");
        femaleDateSet.addColumnValue(new DataSetColumn("FineByAgeAndSexData", "Gender",Integer.class), "Female");

        femaleDateSet.addColumnValue(new DataSetColumn("3munknownAge", "<3 months of ARVs (not MMD)  Unknown Age", Integer.class),getUnknownAgeByGender(0.0,89.0));
        femaleDateSet.addColumnValue(new DataSetColumn("3m<15", "<3 months of ARVs (not MMD) <15", Integer.class),getEnrolledByAgeAndGender(0.0,89.0, 0, 14));
        femaleDateSet.addColumnValue(new DataSetColumn("3m15+", "<3 months of ARVs (not MMD) 15+", Integer.class),getEnrolledByAgeAndGender(0.0,89.0, 15, 200));
				
		femaleDateSet.addColumnValue(new DataSetColumn("5munknownAge", "3-5 months of ARVs Unknown Age", Integer.class),getUnknownAgeByGender(90.0,179.0));
		femaleDateSet.addColumnValue(new DataSetColumn("5m<15", "3-5 months of ARVs <15", Integer.class),getEnrolledByAgeAndGender(90.0,179.0, 0, 14));
		femaleDateSet.addColumnValue(new DataSetColumn("5m15+", "3-5 months of ARVs 15+", Integer.class),getEnrolledByAgeAndGender(90.0,179.0, 15, 200));

		femaleDateSet.addColumnValue(new DataSetColumn("6munknownAge", "6 or more months of ARVs Unknown Age", Integer.class),getUnknownAgeByGender(180.0, 0.0));
		femaleDateSet.addColumnValue(new DataSetColumn("6m<15", "6 or more months of ARVs <15", Integer.class),getEnrolledByAgeAndGender(180.0, 0.0, 0, 14));
		femaleDateSet.addColumnValue(new DataSetColumn("6m15+", "6 or more months of ARVs 15+", Integer.class),getEnrolledByAgeAndGender(180.0, 0.0, 15, 200));

        set.addRow(femaleDateSet);

        obses = getARVDispenced("M");
        DataSetRow maleDataSet = new DataSetRow();
        maleDataSet.addColumnValue(new DataSetColumn("FineByAgeAndSexData", "Gender",Integer.class), "Male");

        maleDataSet.addColumnValue(new DataSetColumn("3munknownAge", "<3 months of ARVs (not MMD)  Unknown Age", Integer.class),getUnknownAgeByGender(0.0,89.0));
        maleDataSet.addColumnValue(new DataSetColumn("3m<15", "<3 months of ARVs (not MMD) <15", Integer.class),getEnrolledByAgeAndGender(0.0,89.0, 0, 14));
        maleDataSet.addColumnValue(new DataSetColumn("3m15+", "<3 months of ARVs (not MMD) 15+", Integer.class),getEnrolledByAgeAndGender(0.0,89.0, 15, 200));
		
		maleDataSet.addColumnValue(new DataSetColumn("5munknownAge", "3-5 months of ARVs Unknown Age", Integer.class),getUnknownAgeByGender(90.0,179.0));
		maleDataSet.addColumnValue(new DataSetColumn("5m<15", "3-5 months of ARVs <15", Integer.class),getEnrolledByAgeAndGender(90.0,179.0, 0, 14));
		maleDataSet.addColumnValue(new DataSetColumn("5m15+", "3-5 months of ARVs 15+", Integer.class),getEnrolledByAgeAndGender(90.0,179.0, 15, 200));

		maleDataSet.addColumnValue(new DataSetColumn("6munknownAge", "6 or more months of ARVs Unknown Age", Integer.class),getUnknownAgeByGender(180.0, 0.0));
		maleDataSet.addColumnValue(new DataSetColumn("6m<15", "6 or more months of ARVs <15", Integer.class),getEnrolledByAgeAndGender(180.0, 0.0, 0, 14));
		maleDataSet.addColumnValue(new DataSetColumn("6m15+", "6 or more months of ARVs 15+", Integer.class),getEnrolledByAgeAndGender(180.0, 0.0, 15, 200));

        set.addRow(maleDataSet);
        return set;
	}
	private int getUnknownAgeByGender(double mind, double maxd){
        int count=0;
        for (Obs obs : obses) {   
			if ( Objects.isNull(obs.getPerson().getAge())|| obs.getPerson().getAge()==0) {
					if (obs.getValueNumeric()<=maxd && obs.getValueNumeric()>=mind){
						count++;
						
					}
					if (mind==0.0 && obs.getValueNumeric() >= maxd){
						count++;
					}
					if (maxd==0.0 && obs.getValueNumeric() >= mind){
						count++;
					}             
            }
        }
        // total = total + count;
        return count;
    }
	private int getEnrolledByAgeAndGender(double mind, double maxd, int min, int max) {
        int count = 0;
        List<Integer> persoIntegers = new ArrayList<>();
        for (Obs obs : obses) {
            if (persoIntegers.contains(obs.getPersonId()))
                continue;
            if (obs.getPerson().getAge() >= min && obs.getPerson().getAge() <= max) {
				if (mind>0 && obs.getValueNumeric()<=maxd && obs.getValueNumeric()>=mind){
					count++;
					
				}
				else if (mind==0 && maxd == 89.0 && obs.getValueNumeric() <= maxd){
					count++;
					
				}
				else if (maxd==0 && mind == 180.0 && obs.getValueNumeric() >= mind){
					count++;
					
				}
				persoIntegers.add(obs.getPersonId());
            }

            
        }
        // total = total + count;
        // clearCountedPerson(persoIntegers);
        return count;
    }
	private void clearCountedPerson(List<Integer> personIds) {
		        for (int pId : personIds) {
		            obses.removeIf(p -> p.getPersonId().equals(pId));
		        }
		    }
	public List<Obs> getARVDispenced(String gender) {
		List<Integer> pList = getTotalEnrolledPatients(gender);
		List<Obs> localObs = new ArrayList<>();
		List<Integer> patients = new ArrayList<>();
		if (pList == null || pList.size() == 0)
			return localObs;
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs").from(Obs.class, "obs").whereEqual("obs.concept", conceptService.getConceptByUuid(ARV_DISPENSED_IN_DAYS)).and().whereIn("obs.personId", pList).whereLess("obs.obsDatetime", hdsd.getEndDate()).orderDesc("obs.personId,obs.obsDatetime");		
		List<Obs> arvObs = evaluationService.evaluateToList(queryBuilder, Obs.class, context);
		for (Obs obs : arvObs) {
			if(!patients.contains(obs.getPersonId()))
				{
				patients.add(obs.getPersonId());
				localObs.add(obs);
				}
		}
		return localObs;	
	}
	
	private List<Integer> getTotalEnrolledPatients(String gender) {

		List<Integer> patientsId = getListOfALiveORRestartPatientObservertions(gender);
		List<Integer> patients = new ArrayList<>();
        if (patientsId == null || patientsId.size() == 0)
                return patients;
        HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
        queryBuilder.select("obs");
        queryBuilder.from(Obs.class, "obs")
        .whereEqual("obs.encounter.encounterType", hdsd.getEncounterType())
        .and()
        .whereEqual("obs.concept", conceptService.getConceptByUuid(TREATMENT_END_DATE))
		.and()
        .whereEqual("obs.person.gender", gender)
        .and()
        .whereGreater("obs.valueDatetime", hdsd.getEndDate())
        .and()
        .whereLess("obs.obsDatetime", hdsd.getEndDate())
        .whereIdIn("obs.personId", patientsId)
        .orderDesc("obs.personId,obs.obsDatetime");
		List<Obs> enrollingpatients = evaluationService.evaluateToList(queryBuilder, Obs.class, context);

		for (Obs obs : enrollingpatients) {
			if (!patients.contains(obs.getPersonId())) {
				patients.add(obs.getPersonId());
			}
		}
		return patients;
	}
	
	private List<Integer> getListOfALiveORRestartPatientObservertions(String gender) {

		List<Integer> uniqiObs = new ArrayList<>();
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();

		queryBuilder.select("obs")
				.from(Obs.class, "obs")
				.whereEqual("obs.encounter.encounterType", hdsd.getEncounterType())
				.and()
        		.whereEqual("obs.person.gender", gender)
                .and()
				.whereEqual("obs.concept", conceptService.getConceptByUuid(PATIENT_STATUS))
				.and()
				.whereIn("obs.valueCoded", Arrays.asList(conceptService.getConceptByUuid(ALIVE),
						conceptService.getConceptByUuid(RESTART)))
				.and().whereLess("obs.obsDatetime", hdsd.getEndDate());
		queryBuilder.orderDesc("obs.personId,obs.obsDatetime");

		List<Obs> aliveObs = evaluationService.evaluateToList(queryBuilder, Obs.class, context);

		for (Obs obs : aliveObs) {
			if (!uniqiObs.contains(obs.getPersonId())) {
				uniqiObs.add(obs.getPersonId());
			}
		}

		return uniqiObs;
	}}
