package org.openmrs.module.ohrireports.reports.datasetevaluator.datim;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.ALIVE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.PATIENT_STATUS;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.RESTART;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TREATMENT_END_DATE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.ARV_DISPENSED_IN_DAYS;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.Concept;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.TxTbDenominatorPositiveResultReturnedDataSetDefinition;
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

@Handler(supports = { TxTbDenominatorPositiveResultReturnedDataSetDefinition.class })
public class TxTbDenominatorPositiveResultReturnedDataSetDefinitionEvaluator implements DataSetEvaluator {
	
	private EvaluationContext context;
	
	private TxTbDenominatorPositiveResultReturnedDataSetDefinition hdsd;
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private EvaluationService evaluationService;
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		
		hdsd = (TxTbDenominatorPositiveResultReturnedDataSetDefinition) dataSetDefinition;
		context = evalContext;
		
		DataSetRow dataSet = new DataSetRow();
		dataSet.addColumnValue(new DataSetColumn("", "", String.class),
		    "Number of ART patients who had a positive result returned for bacteriological diagnosis of active TB disease");
		dataSet.addColumnValue(new DataSetColumn("num", "Num", Integer.class), getPositiveResult());
		SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);
		set.addRow(dataSet);
		return set;
	}
	
	public Integer getPositiveResult(){
		List<Integer> positiveResult = new ArrayList<>();
		List<Obs> obspositiveResult = new ArrayList<>();
		List<Integer> specimenSent = getSpecimentSent();
		if (specimenSent == null || specimenSent.size()==0){
			return positiveResult.size();
		}
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs").from(Obs.class,"obs").whereEqual("obs.concept", conceptService.getConceptByUuid(TB_DIAGNOSTIC_TEST_RESULT)).and().whereEqual("obs.valueCoded", conceptService.getConceptByUuid(POSITIVE)).and().whereIdIn("obs.personId", specimenSent).and().whereLess("obs.obsDatetime", hdsd.getEndDate()).orderDesc("obs.personId, obs.obsDatetime");
		obspositiveResult=evaluationService.evaluateToList(queryBuilder,Obs.class,context);
		for (Obs obs:obspositiveResult){
			if (!positiveResult.contains(obs.getPersonId())){
				positiveResult.add(obs.getPersonId());
			}
		}
		return positiveResult.size();
	}
	
	public List<Integer> getSpecimentSent(){
		List<Integer> specimenSent = new ArrayList<>();
		List<Obs> obsSpecimenSent = new ArrayList<>();
		List<Integer> screened = getTBscreenedInReportingPeriod();
		if (screened == null || screened.size()==0){
			return specimenSent;
		}
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs").from(Obs.class,"obs").whereEqual("obs.concept", conceptService.getConceptByUuid(SPECIMEN_SENT)).and().whereEqual("obs.valueCoded", conceptService.getConceptByUuid(YES)).and().whereIdIn("obs.personId", screened).and().whereLess("obs.obsDatetime", hdsd.getEndDate()).orderDesc("obs.personId, obs.obsDatetime");
		obsSpecimenSent=evaluationService.evaluateToList(queryBuilder,Obs.class,context);
		for (Obs obs:obsSpecimenSent){
			if (!specimenSent.contains(obs.getPersonId())){
				specimenSent.add(obs.getPersonId());
			}
		}
		return specimenSent;
	}
	
	public List<Integer> getTBscreenedInReportingPeriod(){
		List<Integer> tbscreened = new ArrayList<>();
		List<Obs> obstbstarted = new ArrayList<>();
		List<Integer> dispense = getDispenseDose();
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
	
	public List<Integer> getDispenseDose() {
		List<Integer> pList = getDatimValidTratmentEndDatePatients();
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
	
	private List<Integer> getDatimValidTratmentEndDatePatients() {

		List<Integer> patientsId = getListOfALiveORRestartPatientObservertions();
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
		return patients;
	}
	
	private List<Integer> getListOfALiveORRestartPatientObservertions() {

		List<Integer> uniqiObs = new ArrayList<>();
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();

		queryBuilder.select("obs")
				.from(Obs.class, "obs")
				.whereEqual("obs.encounter.encounterType", hdsd.getEncounterType())
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
