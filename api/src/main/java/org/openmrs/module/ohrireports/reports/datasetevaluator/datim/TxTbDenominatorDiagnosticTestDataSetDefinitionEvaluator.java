package org.openmrs.module.ohrireports.reports.datasetevaluator.datim;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.ART_START_DATE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.ALIVE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.PATIENT_STATUS;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.RESTART;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TREATMENT_END_DATE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.ARV_DISPENSED_IN_DAYS;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TB_TREATMENT_START_DATE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.TxTbDenominatorDiagnosticTestDataSetDefinition;
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

@Handler(supports = { TxTbDenominatorDiagnosticTestDataSetDefinition.class })
public class TxTbDenominatorDiagnosticTestDataSetDefinitionEvaluator implements DataSetEvaluator {
	
	private EvaluationContext context;
	
	private TxTbDenominatorDiagnosticTestDataSetDefinition hdsd;
	private int total = 0;
	private int maletotal = 0;
	private int femaletotal = 0;
		
	// HashMap<Integer, Concept> patientStatus = new HashMap<>();
	private String title = "Number of ART patients who were started on TB treatment during the reporting period";
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private EvaluationService evaluationService;
	List<Obs> obses = new ArrayList<>();
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		
		hdsd = (TxTbDenominatorDiagnosticTestDataSetDefinition) dataSetDefinition;
		context = evalContext;
		total=0;
		maletotal = 0;
		femaletotal = 0;
		SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);
		DataSetRow newARTstarted = new DataSetRow();
		obses = getARTstarted("F");
		femaletotal+=obses.size();
		newARTstarted.addColumnValue(new DataSetColumn("", "", String.class),
        "The number of patients starting TB treatment who newly started ART during the reporting period");
		newARTstarted.addColumnValue(new DataSetColumn("funkownAge", "Female Unkown Age", Integer.class),
        getUnknownAgeByGender());
		newARTstarted.addColumnValue(new DataSetColumn("f<15", "Female <15", Integer.class),getEnrolledByAgeAndGender(0, 14));
		newARTstarted.addColumnValue(new DataSetColumn("f+15", "female +15", Integer.class),getEnrolledByAgeAndGender(15, 150));
		obses = getARTstarted("M");
		maletotal+=obses.size();
		newARTstarted.addColumnValue(new DataSetColumn("munkownAge", "Male Unkown Age", Integer.class),
        getUnknownAgeByGender());
		newARTstarted.addColumnValue(new DataSetColumn("m<15", "Male <15", Integer.class),getEnrolledByAgeAndGender(0, 14));
		newARTstarted.addColumnValue(new DataSetColumn("m+15", "Male +15", Integer.class),getEnrolledByAgeAndGender(15, 150));
		set.addRow(newARTstarted);

		DataSetRow oldARTstarted = new DataSetRow();
		obses = getPreviouslyOnART("F");
		femaletotal+=obses.size();
		oldARTstarted.addColumnValue(new DataSetColumn("", "", String.class),
        "The number of patients starting TB treatment who were already on ART prior to the start of the reporting period");
		oldARTstarted.addColumnValue(new DataSetColumn("funkownAge", "Female Unkown Age", Integer.class),
        getUnknownAgeByGender());
		oldARTstarted.addColumnValue(new DataSetColumn("f<15", "Female <15", Integer.class),getEnrolledByAgeAndGender(0, 14));
		oldARTstarted.addColumnValue(new DataSetColumn("f+15", "female +15", Integer.class),getEnrolledByAgeAndGender(15, 150));

		obses = getPreviouslyOnART("M");
		maletotal+=obses.size();
		oldARTstarted.addColumnValue(new DataSetColumn("munkownAge", "Male Unkown Age", Integer.class),
        getUnknownAgeByGender());
		oldARTstarted.addColumnValue(new DataSetColumn("m<15", "Male <15", Integer.class),getEnrolledByAgeAndGender(0, 14));
		oldARTstarted.addColumnValue(new DataSetColumn("m+15", "Male +15", Integer.class),getEnrolledByAgeAndGender(15, 150));
		set.addRow(oldARTstarted);

        DataSetRow tSetRow = new DataSetRow();
		tSetRow.addColumnValue(new DataSetColumn("funkownAge", "feSub-Total", Integer.class),
                femaletotal);
		tSetRow.addColumnValue(new DataSetColumn("munkownAge", "maleSub-Total", Integer.class),
                maletotal);
        tSetRow.addColumnValue(new DataSetColumn("subtotal", "Sub-Total", Integer.class),
                total);
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
	public List<Obs> getPreviouslyOnART(String gender){
		List<Integer> tbstarted = getTBstartedInReportingPeriod(gender);
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
	public List<Obs> getARTstarted(String gender){
		List<Integer> tbstarted = getTBstartedInReportingPeriod(gender);
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

	public List<Integer> getTBstartedInReportingPeriod(String gender){
		List<Integer> tbstarted = new ArrayList<>();
		List<Obs> obstbstarted = new ArrayList<>();
		List<Integer> dispense = getDispenseDose(gender);
		if (dispense == null || dispense.size()==0){
			return tbstarted;
		}
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs").from(Obs.class,"obs").whereEqual("obs.concept", conceptService.getConceptByUuid(TB_TREATMENT_START_DATE)).and().whereGreater("obs.valueDatetime", hdsd.getStartDate()).and().whereLess("obs.valueDatetime",hdsd.getEndDate()).and().whereIdIn("obs.personId", dispense).orderDesc("obs.personId, obs.obsDatetime");
		obstbstarted=evaluationService.evaluateToList(queryBuilder,Obs.class,context);
		for (Obs obs:obstbstarted){
			if (!tbstarted.contains(obs.getPersonId())){
				tbstarted.add(obs.getPersonId());
			}
		}
		return tbstarted;
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
