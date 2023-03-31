package org.openmrs.module.ohrireports.reports.datasetevaluator;

import java.io.ObjectInput;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.HTS_FOLLOW_UP_ENCOUNTER_TYPE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.ALIVE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.RESTART;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TRANSFERRED_UUID;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TRANSFERRED_IN;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.ART_START_DATE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.SERVICE_DELIVERY_POINT_NUMBER_MRN;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.UNIQUE_ANTIRETROVAIRAL_THERAPY_UAN;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.ADHERENCE_UUID;

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.module.ohrireports.reports.datasetdefinition.TransferredInOutDataSetDefinition;
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

/*
 * 
 * =================================================== Report Name ====================================================================
 * Transferred IN/OUT  
 * ================================================== Report objective ===================================================================
 * To list all clients who are transferred to another health facility during the reporting period (TO) 
 * To list all clients who are transferred to the facility from another health facility for ARV treatment during the reporting period (TI) 
 * =======================================================================================================================================
 * =============================================== Columns ===============================================================================
 * Description 
 * Concept ID 
 * Patient Name 
 * MRN 
 * UAN 
 * Age 
 * Sex 
 * ART Start Date 
 * Last Follow-up Date 
 * Follow-up Status
 * Adherence 
 * Regimen 
 * Next Visit Date 
 * Referral Status 
 * Total Patients 
 * ================================================================ Key Assumptions ============================================================== 
 * The report will include all patients who have TO as the value for their follow-up status in the maximum available follow-up record which is greater than or equal to the Reporting Start Date and less than or equal to the Reporting End date. 
 * 
 * The report will also include patients whose Reason for Eligibility is TI in the maximum available follow-up record which is greater than or equal to the Reporting Start Date and less than or equal to the Reporting End date. 
 * The current date (today) will be the Reporting End date if no date filtration criteria is selected. 
 * 
 * The default value for all the filtration criteria will be ‘All’ 
 * The report query range will include data on the start and end date of the reporting period  
 * When counting the number of days between two dates, the count will always include both weekends and public holidays. 
 * The user will have the option to filter the report using the criteria listed on the Filtration Criteria section of this document. 
 * Pseudo Algorithm 
 * Before a patient record may be included into the report dataset, the following pseudo algorithm must pass: 
 * 
 * If ART Started = Yes 
 * 
 *      AND 
 * If there is an ART Start Date and the ART Start Date is <= the reporting end date 
 *      AND 
 * If patient has Follow-up record >=Report Start Date and <=Report End Date 
 *      AND 
 * If follow-up status = TO in the maximum available follow-up record 
 *      OR 
 * If Reason for Eligibility = TI in the maximum available follow-up record 
 *      ONLY THEN 
 * count the record 
 * Filtration Criteria 
 * Regimen - (List of Regimens) 
 * Age – (From…..To…..) 
 * Sex – (All, Male, Female) 
 * Adherence – (All, Good, Fair, Poor) 
 * Referral Status – (All, TI, TO) 
 * TI/TO Date – Date range (From(Date) – To(Date)) 
 * Disaggregation 
 * 
 */
@Handler(supports = { TransferredInOutDataSetDefinition.class })
public class TransferredInOutDataSetDefinitionEvaluator implements DataSetEvaluator {
	
	private TransferredInOutDataSetDefinition tDataSetDefinition;
	
	private EvaluationContext evalContext;
	
	private Concept transferredConcept;
	
	@Autowired
	private EvaluationService evaluationService;
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private EncounterService encounterService;
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		tDataSetDefinition = (TransferredInOutDataSetDefinition) dataSetDefinition;
		this.evalContext = evalContext;
		SimpleDataSet dataSet = new SimpleDataSet(tDataSetDefinition, this.evalContext);
		
		if (tDataSetDefinition.getEndDate().equals(null))
			tDataSetDefinition.setEndDate(Calendar.getInstance().getTime());
		
		List<Patient> patients = getPatientsWithArt();
		transferredConcept = conceptService.getConceptByUuid(TRANSFERRED_UUID);
		for (Patient patient : patients) {
			Obs artStartDate = getArtStartDate(patient);
			
			//exclude if patient is not doesn't have art start date
			if (artStartDate.equals(null))
				continue;
			
			Obs obs = getPatientFollowUpStatus(patient);
			DataSetRow row = new DataSetRow();
			row.addColumnValue(new DataSetColumn("personID", "#", Integer.class), patient.getPersonId());
			row.addColumnValue(new DataSetColumn("name", "Patient Name", String.class), patient.getPerson().getNames());
			row.addColumnValue(new DataSetColumn("MRN", "MRN", String.class), getPatientMRN(patient.getPerson()));
			row.addColumnValue(new DataSetColumn("UAN", "UAN", String.class), getPatientUAN(patient.getPerson()));
			row.addColumnValue(new DataSetColumn("age", "Age", Integer.class), patient.getPerson().getAge());
			row.addColumnValue(new DataSetColumn("gender", "sex", Integer.class), patient.getPerson().getGender());
			row.addColumnValue(new DataSetColumn("art-start-date", "Art Start Date", Date.class),
			    artStartDate.getValueDate());
			row.addColumnValue(new DataSetColumn("last-follow-up", "Last Follow-up Date", Date.class),
			    getLastFollowUp(patient.getPerson()));
			row.addColumnValue(new DataSetColumn("adherence", "Adherence", String.class), getAdherence(patient.getPerson()));
			row.addColumnValue(new DataSetColumn("regimen", "Regimen", String.class),
			    getLastArtRegiment(patient.getPerson()));
			
			if (obs.getValueCoded().equals(transferredConcept)) {
				row.addColumnValue(new DataSetColumn("referral-status", "Referral Status", String.class), transferredConcept
				        .getName().getName());
			} else {
				row.addColumnValue(new DataSetColumn("follow-up-status", "Follow Up Status", String.class), obs
				        .getValueCodedName().getName());
			}
			
			dataSet.addRow(row);
		}
		return dataSet;
	}
	
	/*
	 * Get patients on Art
	 */
	private List<Patient> getPatientsWithArt() {
		
		return evaluationService.evaluateToList(
		    new HqlQueryBuilder()
		            .select("obs.encounter.patient")
		            .from(Obs.class, "obs")
		            .whereEqual("obs.concept", conceptService.getConceptByUuid(ART_START_DATE))
		            .whereBetweenInclusive("obs.valueDatetime", tDataSetDefinition.getStartDate(),
		                tDataSetDefinition.getEndDate()), Patient.class, evalContext);
		
	}
	
	private String getPatientMRN(Person person) {
		return evaluationService.evaluateToObject(
		    new HqlQueryBuilder()
		            .select("obs.valueText")
		            .from(Obs.class, "obs")
		            .whereEqual("obs.concept", conceptService.getConceptByUuid(SERVICE_DELIVERY_POINT_NUMBER_MRN))
		            .and()
		            .whereEqual("obs.person", person)
		            .whereBetweenInclusive("obs.obsDatetime", tDataSetDefinition.getStartDate(),
		                tDataSetDefinition.getEndDate()).limit(1).orderDesc("obs.obsDatetime"), String.class, evalContext);
	}
	
	private String getLastFollowUp(Person person) {
		EncounterType encounterType = new EncounterType();
		encounterType = evaluationService.evaluateToObject(
		    new HqlQueryBuilder()
		            .select("encounter.encounterType")
		            .from(Encounter.class, "encounter")
		            .whereEqual("encounter.patient", person)
		            .whereBetweenInclusive("encounter.encounterDatetime", tDataSetDefinition.getStartDate(),
		                tDataSetDefinition.getEndDate()).limit(1), EncounterType.class, evalContext);
		
		return encounterType.equals(null) ? "" : encounterType.getName();
	}
	
	private String getPatientUAN(Person patient) {
		
		return evaluationService.evaluateToObject(
		    new HqlQueryBuilder()
		            .select("obs.valueText")
		            .from(Obs.class, "obs")
		            .whereEqual("obs.concept", conceptService.getConceptByUuid(UNIQUE_ANTIRETROVAIRAL_THERAPY_UAN))
		            .and()
		            .whereEqual("obs.person", patient)
		            .whereBetweenInclusive("obs.obsDatetime", tDataSetDefinition.getStartDate(),
		                tDataSetDefinition.getEndDate()).limit(1).orderDesc("obs.obsDatetime"), String.class, evalContext);
		
	}
	
	private String getAdherence(Person patient) {
		Concept adConcept = evaluationService.evaluateToObject(
		    new HqlQueryBuilder()
		            .select("obs.valueCoded")
		            .from(Obs.class, "obs")
		            .whereEqual("obs.concept", conceptService.getConceptByUuid(ADHERENCE_UUID))
		            .whereBetweenInclusive("obs.obsDatetime", tDataSetDefinition.getStartDate(),
		                tDataSetDefinition.getEndDate()).limit(1), Concept.class, evalContext);
		return adConcept.equals(null) ? "" : adConcept.getName().getName();
	}
	
	private String getLastArtRegiment(Person person) {
		
		return evaluationService.evaluateToObject(
		    new HqlQueryBuilder()
		            .select("obs.")
		            .from(Obs.class, "obs")
		            .whereEqual("obs.person", person)
		            .whereBetweenInclusive("obs.obsDatetime", tDataSetDefinition.getStartDate(),
		                tDataSetDefinition.getEndDate()).limit(1).orderDesc("obs.obsDatetime"), String.class, evalContext);
	}
	
	private Obs getArtStartDate(Patient patient) {
		if (tDataSetDefinition.getStartDate().equals(null))
			return evaluationService.evaluateToObject(
			    new HqlQueryBuilder().select("obs").from(Obs.class, "obs.valueDatetime")
			            .whereEqual("obs.concept", conceptService.getConceptByUuid(ART_START_DATE))
			            .whereLessOrEqualTo("obs.obsDatetime", tDataSetDefinition.getEndDate()).orderDesc("obs.obsDatetime")
			            .limit(1), Obs.class, evalContext);
		
		return evaluationService.evaluateToObject(
		    new HqlQueryBuilder()
		            .select("obs")
		            .from(Obs.class, "obs.valueDatetime")
		            .whereEqual("obs.concept", conceptService.getConceptByUuid(ART_START_DATE))
		            .whereBetweenInclusive("obs.obsDatetime", tDataSetDefinition.getStartDate(),
		                tDataSetDefinition.getEndDate()).orderDesc("obs.obsDatetime").limit(1), Obs.class, evalContext);
	}
	
	private Obs getPatientFollowUpStatus(Patient patient) {
		
		return evaluationService.evaluateToObject(
		    new HqlQueryBuilder()
		            .select("obs")
		            .from(Obs.class, "obs")
		            .whereEqual("obs.encounter.encounterType",
		                encounterService.getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE))
		            .whereIn(
		                "obs.valueCoded",
		                Arrays.asList(conceptService.getConceptByUuid(ALIVE), conceptService.getConceptByUuid(RESTART),
		                    conceptService.getConceptByUuid(ALIVE), transferredConcept,
		                    conceptService.getConceptByUuid(TRANSFERRED_IN)))
		            .and()
		            .whereEqual("obs.person", patient.getPerson())
		            .whereBetweenInclusive("obs.obsDatetime", tDataSetDefinition.getStartDate(),
		                tDataSetDefinition.getEndDate()).orderAsc("obs.obsDatetime").limit(1), Obs.class, evalContext);
		
	}
	
}
