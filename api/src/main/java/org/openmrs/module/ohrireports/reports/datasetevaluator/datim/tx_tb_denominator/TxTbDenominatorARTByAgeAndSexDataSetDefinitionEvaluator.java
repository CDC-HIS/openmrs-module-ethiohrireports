package org.openmrs.module.ohrireports.reports.datasetevaluator.datim.tx_tb_denominator;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.ART_START_DATE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.ALIVE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.PATIENT_STATUS;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.RESTART;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TREATMENT_END_DATE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.ARV_DISPENSED_IN_DAYS;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TB_SCREENING_DATE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TB_DIAGNOSTIC_TEST_RESULT;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.tx_tb_denominator.TxTbDenominatorARTByAgeAndSexDataSetDefinition;
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

@Handler(supports = { TxTbDenominatorARTByAgeAndSexDataSetDefinition.class })
public class TxTbDenominatorARTByAgeAndSexDataSetDefinitionEvaluator implements DataSetEvaluator {
	
	private EvaluationContext context;
	
	private TxTbDenominatorARTByAgeAndSexDataSetDefinition hdsd;
	private int total = 0;
	private int malePretotal = 0;
	private int femalePretotal = 0;
	private int maleNewtotal = 0;
	private int femaleNewtotal = 0;	
	// HashMap<Integer, Concept> patientStatus = new HashMap<>();
	private String title = "Number of ART patients who were started on TB treatment during the reporting period";
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private EvaluationService evaluationService;
	List<Obs> obses = new ArrayList<>();
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		
		hdsd = (TxTbDenominatorARTByAgeAndSexDataSetDefinition) dataSetDefinition;
		context = evalContext;
		total=0;
		malePretotal = 0;
		femalePretotal = 0;
		maleNewtotal = 0;
		femaleNewtotal = 0;
		SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);
		DataSetRow allPositive = new DataSetRow();
		obses = getARTstarted("F","p");
		femaleNewtotal+=obses.size();
		allPositive.addColumnValue(new DataSetColumn("", "", String.class),
        "POSITIVE");
		allPositive.addColumnValue(new DataSetColumn("funknownAgeN", "Newly enrolled on ART Female Unknown Age", Integer.class),
        getUnknownAgeByGender());
		allPositive.addColumnValue(new DataSetColumn("f<15N", "Newly enrolled on ART Female <15", Integer.class),getEnrolledByAgeAndGender(0, 14));
		allPositive.addColumnValue(new DataSetColumn("f+15N", "Newly enrolled on ART Female +15", Integer.class),getEnrolledByAgeAndGender(15, 150));
		obses = getARTstarted("M","p");
		maleNewtotal+=obses.size();
		allPositive.addColumnValue(new DataSetColumn("munknownAgeN", "Newly enrolled on ART Male Unknown Age", Integer.class),
        getUnknownAgeByGender());
		allPositive.addColumnValue(new DataSetColumn("m<15N", "Newly enrolled on ART Male <15", Integer.class),getEnrolledByAgeAndGender(0, 14));
		allPositive.addColumnValue(new DataSetColumn("m+15N", "Newly enrolled on ART Male +15", Integer.class),getEnrolledByAgeAndGender(15, 150));

		obses = getPreviouslyOnART("F","p");
		femalePretotal+=obses.size();
		allPositive.addColumnValue(new DataSetColumn("funknownAgeP", "Previously Enrolled on ART Female Unknown Age", Integer.class),
        getUnknownAgeByGender());
		allPositive.addColumnValue(new DataSetColumn("f<15P", "Previously Enrolled on ART Female <15", Integer.class),getEnrolledByAgeAndGender(0, 14));
		allPositive.addColumnValue(new DataSetColumn("f+15P", "Previously Enrolled on ART Female +15", Integer.class),getEnrolledByAgeAndGender(15, 150));

		obses = getPreviouslyOnART("M","p");
		malePretotal+=obses.size();
		allPositive.addColumnValue(new DataSetColumn("munknownAgeP", "Previously Enrolled on ART Male Unknown Age", Integer.class),
        getUnknownAgeByGender());
		allPositive.addColumnValue(new DataSetColumn("m<15P", "Previously Enrolled on ART Male <15", Integer.class),getEnrolledByAgeAndGender(0, 14));
		allPositive.addColumnValue(new DataSetColumn("m+15P", "Previously Enrolled on ART Male +15", Integer.class),getEnrolledByAgeAndGender(15, 150));
		set.addRow(allPositive);

		DataSetRow allNegative = new DataSetRow();
		obses = getARTstarted("F","n");
		femaleNewtotal+=obses.size();
		allNegative.addColumnValue(new DataSetColumn("", "", String.class),
        "NEGATIVE");
		allNegative.addColumnValue(new DataSetColumn("funknownAgeN", "Newly enrolled on ART Female Unknown Age", Integer.class),
        getUnknownAgeByGender());
		allNegative.addColumnValue(new DataSetColumn("f<15N", "Newly enrolled on ART Female <15", Integer.class),getEnrolledByAgeAndGender(0, 14));
		allNegative.addColumnValue(new DataSetColumn("f+15N", "Newly enrolled on ART Female +15", Integer.class),getEnrolledByAgeAndGender(15, 150));
		
		obses = getARTstarted("M","n");
		maleNewtotal+=obses.size();
		allNegative.addColumnValue(new DataSetColumn("munknownAgeN", "Newly enrolled on ART Male Unknown Age", Integer.class),
        getUnknownAgeByGender());
		allNegative.addColumnValue(new DataSetColumn("m<15N", "Newly enrolled on ART Male <15", Integer.class),getEnrolledByAgeAndGender(0, 14));
		allNegative.addColumnValue(new DataSetColumn("m+15N", "Newly enrolled on ART Male +15", Integer.class),getEnrolledByAgeAndGender(15, 150));

		obses = getPreviouslyOnART("F","n");
		femalePretotal+=obses.size();
		allNegative.addColumnValue(new DataSetColumn("funknownAgeP", "Previously Enrolled on ART Female Unknown Age", Integer.class),
        getUnknownAgeByGender());
		allNegative.addColumnValue(new DataSetColumn("f<15P", "Previously Enrolled on ART Female <15", Integer.class),getEnrolledByAgeAndGender(0, 14));
		allNegative.addColumnValue(new DataSetColumn("f+15P", "Previously Enrolled on ART Female +15", Integer.class),getEnrolledByAgeAndGender(15, 150));

		obses = getPreviouslyOnART("M","n");
		malePretotal+=obses.size();
		allNegative.addColumnValue(new DataSetColumn("munknownAgeP", "Previously Enrolled on ART Male Unknown Age", Integer.class),
        getUnknownAgeByGender());
		allNegative.addColumnValue(new DataSetColumn("m<15P", "Previously Enrolled on ART Male <15", Integer.class),getEnrolledByAgeAndGender(0, 14));
		allNegative.addColumnValue(new DataSetColumn("m+15P", "Previously Enrolled on ART Male +15", Integer.class),getEnrolledByAgeAndGender(15, 150));
		set.addRow(allNegative);
	
        DataSetRow tSetRow = new DataSetRow();
		tSetRow.addColumnValue(new DataSetColumn("", "", String.class),
                "Sub-total");
		tSetRow.addColumnValue(new DataSetColumn("f<15N", "f<15N", Integer.class),femaleNewtotal);
		tSetRow.addColumnValue(new DataSetColumn("m<15N", "m<15N", Integer.class),maleNewtotal);
		tSetRow.addColumnValue(new DataSetColumn("f+15P", "f+15P", Integer.class),femalePretotal);
		tSetRow.addColumnValue(new DataSetColumn("m<15P", "m<15P", Integer.class),malePretotal);
        set.addRow(tSetRow);
	
		return set;
	}
	private int getUnknownAgeByGender(){
        int count=0;
        for (Obs obs : obses) {   
                if ( Objects.isNull(obs.getPerson().getAge())|| obs.getPerson().getAge()==0) {
                count++;
            }
        }
        total = total + count;
        return count;
    }
	private int getEnrolledByAgeAndGender(int min, int max) {
        int count = 0;
        for (Obs obs : obses) {
            
            if (obs.getPerson().getAge() >= min && obs.getPerson().getAge() <= max) {
                count++;
            }

        }
        total = total + count;
       
        return count;
    }
	public List<Obs> getPreviouslyOnART(String gender, String result){
		List<Integer> tbstarted = new ArrayList<>();
		if (result == "p"){
			tbstarted = getTBscreenedPositive(gender);
		}
		else{
			tbstarted = getTBscreenedNegative(gender);
		}
		List<Obs> obsARTstarted = new ArrayList<>();
		List<Integer> artstarted = new ArrayList<>();
		if (tbstarted==null || tbstarted.size() ==0){
			return obsARTstarted;
		}
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs").from(Obs.class,"obs").whereEqual("obs.concept", conceptService.getConceptByUuid(ART_START_DATE)).and().whereLess("obs.valueDatetime", hdsd.getStartDate()).and().whereIdIn("obs.personId", tbstarted).orderDesc("obs.personId, obs.obsDatetime");
		for (Obs obs: evaluationService.evaluateToList(queryBuilder, Obs.class, context)){
				if (!artstarted.contains(obs.getPersonId())){
					artstarted.add(obs.getPersonId());
					obsARTstarted.add(obs);

				}
		}
		return obsARTstarted;
	}
	public List<Obs> getARTstarted(String gender, String result){
		List<Integer> tbstarted = new ArrayList<>();
		if (result == "p"){
			tbstarted = getTBscreenedPositive(gender);
		}
		else{
			tbstarted = getTBscreenedNegative(gender);
		}
		
		List<Obs> obsARTstarted = new ArrayList<>();
		List<Integer> artstarted = new ArrayList<>();
		if (tbstarted==null || tbstarted.size() ==0){
			return obsARTstarted;
		}
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs").from(Obs.class,"obs").whereEqual("obs.concept", conceptService.getConceptByUuid(ART_START_DATE)).and().whereGreater("obs.valueDatetime", hdsd.getStartDate()).and().whereLess("obs.valueDatetime", hdsd.getEndDate()).and().whereIdIn("obs.personId", tbstarted).orderDesc("obs.personId, obs.obsDatetime");
		for (Obs obs: evaluationService.evaluateToList(queryBuilder, Obs.class, context)){
				if (!artstarted.contains(obs.getPersonId())){
					artstarted.add(obs.getPersonId());
					obsARTstarted.add(obs);

				}
		}
		return obsARTstarted;
	}
	public List<Integer> getTBscreenedNegative(String gender){
		List<Integer> tbscreened = new ArrayList<>();
		List<Obs> obstbstarted = new ArrayList<>();
		List<Integer> dispense = getTBscreenedInReportingPeriod(gender);
		if (dispense == null || dispense.size()==0){
			return tbscreened;
		}
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs").from(Obs.class,"obs").whereEqual("obs.concept", conceptService.getConceptByUuid(TB_DIAGNOSTIC_TEST_RESULT)).and().whereEqual("obs.valueCoded", conceptService.getConceptByUuid(NEGATIVE)).and().whereIdIn("obs.personId", dispense).orderDesc("obs.personId, obs.obsDatetime");
		obstbstarted=evaluationService.evaluateToList(queryBuilder,Obs.class,context);
		for (Obs obs:obstbstarted){
			if (!tbscreened.contains(obs.getPersonId())){
				tbscreened.add(obs.getPersonId());
			}
		}
		return tbscreened;
	}

	public List<Integer> getTBscreenedPositive(String gender){
		List<Integer> tbscreened = new ArrayList<>();
		List<Obs> obstbstarted = new ArrayList<>();
		List<Integer> dispense = getTBscreenedInReportingPeriod(gender);
		if (dispense == null || dispense.size()==0){
			return tbscreened;
		}
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs").from(Obs.class,"obs").whereEqual("obs.concept", conceptService.getConceptByUuid(TB_DIAGNOSTIC_TEST_RESULT)).and().whereEqual("obs.valueCoded", conceptService.getConceptByUuid(POSITIVE)).and().whereIdIn("obs.personId", dispense).orderDesc("obs.personId, obs.obsDatetime");
		obstbstarted=evaluationService.evaluateToList(queryBuilder,Obs.class,context);
		for (Obs obs:obstbstarted){
			if (!tbscreened.contains(obs.getPersonId())){
				tbscreened.add(obs.getPersonId());
			}
		}
		return tbscreened;
	}

	public List<Integer> getTBscreenedInReportingPeriod(String gender){
		List<Integer> tbscreened = new ArrayList<>();
		List<Obs> obstbstarted = new ArrayList<>();
		List<Integer> dispense = getDispenseDose(gender);
		if (dispense == null || dispense.size()==0){
			return tbscreened;
		}
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs").from(Obs.class,"obs").whereEqual("obs.concept", conceptService.getConceptByUuid(TB_SCREENING_DATE)).and().whereGreater("obs.valueDatetime", hdsd.getStartDate()).and().whereLess("obs.valueDatetime",hdsd.getEndDate()).and().whereIdIn("obs.personId", dispense).orderDesc("obs.personId, obs.obsDatetime");
		obstbstarted=evaluationService.evaluateToList(queryBuilder,Obs.class,context);
		for (Obs obs:obstbstarted){
			if (!tbscreened.contains(obs.getPersonId())){
				tbscreened.add(obs.getPersonId());
			}
		}
		return tbscreened;
	}

	public List<Integer> getDispenseDose(String gender) {
		List<Integer> pList = getDatimValidTratmentEndDatePatients(gender);
		List<Integer> patients = new ArrayList<>();
		if (pList == null || pList.size() == 0)
			return patients;
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs").from(Obs.class, "obs").whereEqual("obs.concept", conceptService.getConceptByUuid(ARV_DISPENSED_IN_DAYS)).and().whereIn("obs.personId", pList).whereLess("obs.obsDatetime", hdsd.getEndDate()).orderDesc("obs.personId,obs.obsDatetime");		
		List<Obs> arvObs = evaluationService.evaluateToList(queryBuilder, Obs.class, context);
		for (Obs obs : arvObs) {
			if(!patients.contains(obs.getPersonId()))
				{
				patients.add(obs.getPersonId());
				}
		}
		return patients;	
	}
	
	private List<Integer> getDatimValidTratmentEndDatePatients(String gender) {

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
        .whereGreater("obs.valueDatetime", hdsd.getEndDate())
        .and()
        .whereLess("obs.obsDatetime", hdsd.getEndDate())
        .whereIdIn("obs.personId", patientsId)
        .orderDesc("obs.personId,obs.obsDatetime");
        for (Obs obs : evaluationService.evaluateToList(queryBuilder, Obs.class, context)) {
                if(!patients.contains(obs.getPersonId()))
                        {
                        patients.add(obs.getPersonId());
                        }
        }
		// patients = evaluationService.evaluateToList(queryBuilder, Integer.class, context);
				
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
				.whereIn("obs.valueCoded", Arrays.asList(conceptService.getConceptByUuid(ALIVE),conceptService.getConceptByUuid(RESTART)))
				.and().whereLess("obs.obsDatetime", hdsd.getEndDate());
		queryBuilder.orderDesc("obs.personId,obs.obsDatetime");

		List<Obs> liveObs = evaluationService.evaluateToList(queryBuilder, Obs.class, context);

		for (Obs obs : liveObs) {
			if (!uniqiObs.contains(obs.getPersonId())) {
				uniqiObs.add(obs.getPersonId());
				// patientStatus.put(obs.getPersonId(), obs.getValueCoded());
			}
		}

		return uniqiObs;
	}
}
