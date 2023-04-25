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
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private EvaluationService evaluationService;
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		
		hdsd = (TxTbDenominatorDiagnosticTestDataSetDefinition) dataSetDefinition;
		context = evalContext;
		
		DataSetRow dataSet = new DataSetRow();
		dataSet.addColumnValue(new DataSetColumn("", "", String.class),
		    "Number of ART patients whose specimen were sent for the following diagnosis test");
		dataSet.addColumnValue(new DataSetColumn("smear", "Smear Only", Integer.class), getSmearOnly());
		dataSet.addColumnValue(new DataSetColumn("mwrd",
		        "mWRD : Molecular WHO Recommended Diagnostic PCR (with or without other testing)", Integer.class), getmwrd());
		dataSet.addColumnValue(new DataSetColumn("additional", "Additional test Other than mWRD ", Integer.class),
		    getOtherthanmwrd());
		SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);
		set.addRow(dataSet);
		return set;
	}
	
	public Integer getSmearOnly(){
		List<Integer> smearOnly = new ArrayList<>();
		List<Obs> obsSmearOnly = new ArrayList<>();
		List<Integer> specimenSents = getTBscreenedInReportingPeriod();
		
		if (specimenSents == null || specimenSents.size()==0){
			return smearOnly.size();
		}
		
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs").from(Obs.class,"obs").whereEqual("obs.concept", conceptService.getConceptByUuid(DIAGNOSTIC_TEST)).and().whereEqual("obs.valueCoded", conceptService.getConceptByUuid(SMEAR_ONLY)).and().whereIdIn("obs.personId", specimenSents).and().whereLess("obs.obsDatetime", hdsd.getEndDate()).orderDesc("obs.personId, obs.obsDatetime");
		obsSmearOnly=evaluationService.evaluateToList(queryBuilder,Obs.class,context);
		for (Obs obs:obsSmearOnly){
			if (!smearOnly.contains(obs.getPersonId())){
				smearOnly.add(obs.getPersonId());
			}
		}
		return smearOnly.size();
	}
	
	public Integer getmwrd(){
		List<Integer> mwrd = new ArrayList<>();
		List<Obs> obsmwrd = new ArrayList<>();
		List<Integer> specimenSents = getTBscreenedInReportingPeriod();
		if (specimenSents == null || specimenSents.size()==0){
			return mwrd.size();
		}
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs").from(Obs.class,"obs").whereEqual("obs.concept", conceptService.getConceptByUuid(DIAGNOSTIC_TEST)).and().whereIn("obs.valueCoded", Arrays.asList(conceptService.getConceptByUuid(MTB_RIF_ASSAY_WITH_OTHEROUT_TESTING),conceptService.getConceptByUuid(MTB_RIF_ASSAY_WITH_OTHER_TESTING),conceptService.getConceptByUuid(LF_LAM_MTB_RIF),conceptService.getConceptByUuid(LF_LAM))).and().whereIdIn("obs.personId", specimenSents).and().whereLess("obs.obsDatetime", hdsd.getEndDate()).orderDesc("obs.personId, obs.obsDatetime");
		obsmwrd=evaluationService.evaluateToList(queryBuilder,Obs.class,context);
		for (Obs obs:obsmwrd){
			if (!mwrd.contains(obs.getPersonId())){
				mwrd.add(obs.getPersonId());
			}
		}
		return mwrd.size();
	}
	
	public Integer getOtherthanmwrd(){
		List<Integer> otherthanmwrd = new ArrayList<>();
		List<Obs> obsotherthanmwrd = new ArrayList<>();
		List<Integer> specimenSents = getTBscreenedInReportingPeriod();
		if (specimenSents == null || specimenSents.size()==0){
			return otherthanmwrd.size();
		}
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs").from(Obs.class,"obs").whereEqual("obs.concept", conceptService.getConceptByUuid(DIAGNOSTIC_TEST)).and().whereEqual("obs.valueCoded", conceptService.getConceptByUuid(ADDITIONAL_TEST_OTHERTHAN_GENE_XPERT)).and().whereIdIn("obs.personId", specimenSents).and().whereLess("obs.obsDatetime", hdsd.getEndDate()).orderDesc("obs.personId, obs.obsDatetime");
		obsotherthanmwrd=evaluationService.evaluateToList(queryBuilder,Obs.class,context);
		for (Obs obs:obsotherthanmwrd){
			if (!otherthanmwrd.contains(obs.getPersonId())){
				otherthanmwrd.add(obs.getPersonId());
			}
		}
		return otherthanmwrd.size();
	}
	
	public Integer getSpecimentSent(){
		List<Integer> specimenSent = new ArrayList<>();
		List<Obs> obsSpecimenSent = new ArrayList<>();
		List<Integer> screened = getTBscreenedInReportingPeriod();
		if (screened == null || screened.size()==0){
			return specimenSent.size();
		}
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs").from(Obs.class,"obs").whereEqual("obs.concept", conceptService.getConceptByUuid(SPECIMEN_SENT)).and().whereEqual("obs.valueCoded", conceptService.getConceptByUuid(YES)).and().whereIdIn("obs.personId", screened).and().whereLess("obs.obsDatetime", hdsd.getEndDate()).orderDesc("obs.personId, obs.obsDatetime");
		obsSpecimenSent=evaluationService.evaluateToList(queryBuilder,Obs.class,context);
		for (Obs obs:obsSpecimenSent){
			if (!specimenSent.contains(obs.getPersonId())){
				specimenSent.add(obs.getPersonId());
			}
		}
		return specimenSent.size();
		

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
		List<Integer> pList = getDatimValidTreatmentEndDatePatients();
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
	
	private List<Integer> getDatimValidTreatmentEndDatePatients() {

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
