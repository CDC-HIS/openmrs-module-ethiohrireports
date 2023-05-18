// /**
//  * This Source Code Form is subject to the terms of the Mozilla Public License,
//  * v. 2.0. If a copy of the MPL was not distributed with this file, You can
//  * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
//  * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
//  * <p>
//  * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
//  * graphic logo is a trademark of OpenMRS Inc.
//  */
// package org.openmrs.module.ohrireports.reports.covid19;

// import org.openmrs.api.ConceptService;
// import org.openmrs.api.EncounterService;
// import org.openmrs.module.ohrireports.reports.converter.OHRIDataConverter;
// import org.openmrs.module.ohrireports.reports.library.EncounterDataLibrary;
// import org.openmrs.module.ohrireports.reports.library.PatientDataLibrary;
// import org.openmrs.module.reporting.cohort.definition.EncounterCohortDefinition;
// import org.openmrs.module.reporting.data.converter.AgeConverter;
// import org.openmrs.module.reporting.data.converter.ObsValueConverter;
// import org.openmrs.module.reporting.data.encounter.definition.EncounterIdDataDefinition;
// import org.openmrs.module.reporting.data.patient.definition.PatientIdDataDefinition;
// import org.openmrs.module.reporting.dataset.definition.EncounterDataSetDefinition;
// import org.openmrs.module.reporting.evaluation.parameter.Mapped;
// import org.openmrs.module.reporting.evaluation.parameter.Parameter;
// import org.openmrs.module.reporting.query.encounter.definition.CodedObsForEncounterQuery;
// import org.openmrs.module.reporting.query.encounter.definition.CompositionEncounterQuery;
// import org.openmrs.module.reporting.query.encounter.definition.EncounterQuery;
// import org.openmrs.module.reporting.report.ReportDesign;
// import org.openmrs.module.reporting.report.ReportRequest;
// import org.openmrs.module.reporting.report.definition.ReportDefinition;
// import org.openmrs.module.reporting.report.manager.ReportManager;
// import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;

// import java.util.Arrays;
// import java.util.Collections;
// import java.util.List;

// import static org.openmrs.module.ohrireports.OHRIReportsConstants.*;

// @Component
// public class Covid19Report implements ReportManager {

// 	@Autowired
// 	EncounterService encounterService;

// 	@Autowired
// 	ConceptService conceptService;

// 	@Autowired
// 	PatientDataLibrary pdl;

// 	@Autowired
// 	EncounterDataLibrary edl;

// 	@Autowired
// 	OHRIDataConverter odc;

// 	@Override
// 	public String getUuid() {
// 		return COVID19_REPORT_UUID;
// 	}

// 	@Override
// 	public String getName() {
// 		return "COVID-19 Report";
// 	}

// 	@Override
// 	public String getDescription() {
// 		return null;
// 	}

// 	@Override
// 	public List<Parameter> getParameters() {
// 		return null;
// 	}

// 	@Override
// 	public ReportDefinition constructReportDefinition() {
// 		ReportDefinition reportDefinition = new ReportDefinition();
// 		reportDefinition.setUuid(getUuid());
// 		reportDefinition.setName(getName());
// 		reportDefinition.setDescription(getDescription());
// 		reportDefinition.setParameters(getParameters());

// 		EncounterCohortDefinition ecd = new EncounterCohortDefinition();
// 		ecd.addEncounterType(encounterService.getEncounterTypeByUuid(COVID_ASSESSMENT_ENCOUNTER_TYPE));
// 		reportDefinition.setBaseCohortDefinition(Mapped.mapStraightThrough(ecd));

// 		EncounterDataSetDefinition edsd = new EncounterDataSetDefinition();
// 		edsd.addRowFilter(getEncounterQuery());
// 		addColumns(edsd);

// 		reportDefinition.addDataSetDefinition("COVID-19", edsd, Collections.EMPTY_MAP);
// 		return reportDefinition;
// 	}

// 	private void addColumns(EncounterDataSetDefinition edsd) {
// 		edsd.addColumn("Client ID", new PatientIdDataDefinition(), "");
// 		edsd.addColumn("Name", pdl.getName(), "");
// 		edsd.addColumn("Age", pdl.getAge(), "", new AgeConverter());
// 		edsd.addColumn("Sex", pdl.getGender(), "");
// 		edsd.addColumn("Encounter ID", new EncounterIdDataDefinition(), "");
// 		edsd.addColumn("Assessment Date", edl.getObsValue(COVID19_ASSESSMENT_DATE), "", new ObsValueConverter());
// 		//edsd.addColumn("Assessment Reasons", edl.getObsValues(COVID19_ASSESSMENT_REASON), "",odc.getObsValuesCodedPipeDelimitedConverter());
// 		addColumnAsTrueOrFalse(edsd, "Assessment Contact Case", COVID19_ASSESSMENT_REASON, COVID19_ASSESSMENT_CONTACT_CASE);
// 		addColumnAsTrueOrFalse(edsd, "Assessment Entry Country", COVID19_ASSESSMENT_REASON, COVID19_ASSESSMENT_ENTRY_COUNTRY);
// 		addColumnAsTrueOrFalse(edsd, "Assessment Follow Up", COVID19_ASSESSMENT_REASON, COVID19_ASSESSMENT_FOLLOW_UP);
// 		addColumnAsTrueOrFalse(edsd, "Assessment Health Worker", COVID19_ASSESSMENT_REASON, COVID19_ASSESSMENT_HEALTH_WORKER);
// 		addColumnAsTrueOrFalse(edsd, "Assessment Frontline Worker", COVID19_ASSESSMENT_REASON,
// 		    COVID19_ASSESSMENT_FRONTLINE_WORKER);
// 		addColumnAsTrueOrFalse(edsd, "Assessment Post Mortem", COVID19_ASSESSMENT_REASON, COVID19_ASSESSMENT_POST_MORTEM);
// 		addColumnAsTrueOrFalse(edsd, "Assessment Quarantine", COVID19_ASSESSMENT_REASON, COVID19_ASSESSMENT_QUARANTINE);
// 		addColumnAsTrueOrFalse(edsd, "Assessment RDT Confirmatory", COVID19_ASSESSMENT_REASON,
// 		    COVID19_ASSESSMENT_RDT_CONFIRMATORY);
// 		addColumnAsTrueOrFalse(edsd, "Assessment Surveillance", COVID19_ASSESSMENT_REASON, COVID19_ASSESSMENT_SURVEILLANCE);
// 		addColumnAsTrueOrFalse(edsd, "Assessment Symptomatic", COVID19_ASSESSMENT_REASON, COVID19_ASSESSMENT_SYMPTOMATIC);
// 		addColumnAsTrueOrFalse(edsd, "Assessment Travel Out Country", COVID19_ASSESSMENT_REASON,
// 		    COVID19_ASSESSMENT_TRAVEL_OUT_COUNTRY);
// 		addColumnAsTrueOrFalse(edsd, "Assessment Voluntary", COVID19_ASSESSMENT_REASON, COVID19_ASSESSMENT_VOLUNTARY);
// 		addColumnAsTrueOrFalse(edsd, "Assessment Other", COVID19_ASSESSMENT_REASON, COVID19_ASSESSMENT_OTHER);
// 		edsd.addColumn("Assessment Presentation", edl.getObsValue(COVID19_ASSESSMENT_PRESENTATION), "",
// 		    new ObsValueConverter());
// 		edsd.addColumn("Date Onset Symptoms", edl.getObsValue(COVID19_DATE_ONSET_SYMPTOMS), "", new ObsValueConverter());
// 		//edsd.addColumn("Presenting Symptoms", edl.getObsValues(COVID19_PRESENTING_SYMPTOMS), "",odc.getObsValuesCodedPipeDelimitedConverter());
// 		addColumnAsTrueOrFalse(edsd, "Symptom Abdominal Pain", COVID19_PRESENTING_SYMPTOMS, COVID19_SYMPTOM_ABDOMINAL_PAIN);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Chest Pain", COVID19_PRESENTING_SYMPTOMS, COVID19_SYMPTOM_CHEST_PAIN);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Cough", COVID19_PRESENTING_SYMPTOMS, COVID19_SYMPTOM_COUGH);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Diarrhoea", COVID19_PRESENTING_SYMPTOMS, COVID19_SYMPTOM_DIARRHOEA);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Disturbance Consciousness", COVID19_PRESENTING_SYMPTOMS,
// 		    COVID19_SYMPTOM_DISTURBANCE_CONSCIOUSNESS);
// 		addColumnAsTrueOrFalse(edsd, "Symptom General Weakness", COVID19_PRESENTING_SYMPTOMS,
// 		    COVID19_SYMPTOM_GENERAL_WEAKNESS);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Headache", COVID19_PRESENTING_SYMPTOMS, COVID19_SYMPTOM_HEADACHE);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Fever Chills", COVID19_PRESENTING_SYMPTOMS, COVID19_SYMPTOM_FEVER_CHILLS);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Irritability Confusion", COVID19_PRESENTING_SYMPTOMS,
// 		    COVID19_SYMPTOM_IRRITABILITY_CONFUSION);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Joint Pain", COVID19_PRESENTING_SYMPTOMS, COVID19_SYMPTOM_JOINT_PAIN);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Loss Smell", COVID19_PRESENTING_SYMPTOMS, COVID19_SYMPTOM_LOSS_SMELL);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Loss Taste", COVID19_PRESENTING_SYMPTOMS, COVID19_SYMPTOM_LOSS_TASTE);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Muscular Pain", COVID19_PRESENTING_SYMPTOMS, COVID19_SYMPTOM_MUSCULAR_PAIN);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Nausea Vomiting", COVID19_PRESENTING_SYMPTOMS, COVID19_SYMPTOM_NAUSEA_VOMITING);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Red Eyes", COVID19_PRESENTING_SYMPTOMS, COVID19_SYMPTOM_RED_EYES);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Runny Nose", COVID19_PRESENTING_SYMPTOMS, COVID19_SYMPTOM_RUNNY_NOSE);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Shortness Breath", COVID19_PRESENTING_SYMPTOMS,
// 		    COVID19_SYMPTOM_SHORTNESS_BREATH);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Sneezing", COVID19_PRESENTING_SYMPTOMS, COVID19_SYMPTOM_SNEEZING);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Sore Throat", COVID19_PRESENTING_SYMPTOMS, COVID19_SYMPTOM_SORE_THROAT);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Tiredness", COVID19_PRESENTING_SYMPTOMS, COVID19_SYMPTOM_TIREDNESS);
// 		addColumnAsTrueOrFalse(edsd, "Symptom Other", COVID19_PRESENTING_SYMPTOMS, COVID19_SYMPTOM_OTHER);
// 		edsd.addColumn("Comorbidity Present", edl.getObsValue(COVID19_COMORBIDITY_PRESENT), "", new ObsValueConverter());
// 		//edsd.addColumn("Comorbidity Conditions", edl.getObsValues(COVID19_COMORBIDITY_CONDITION), "",odc.getObsValuesCodedPipeDelimitedConverter());
// 		addColumnAsTrueOrFalse(edsd, "Comorbidity Cardiovascular", COVID19_COMORBIDITY_CONDITION,
// 		    COVID19_COMORBIDITY_CARDIOVASCULAR);
// 		addColumnAsTrueOrFalse(edsd, "Comorbidity Chronic Lung", COVID19_COMORBIDITY_CONDITION,
// 		    COVID19_COMORBIDITY_CHRONIC_LUNG);
// 		addColumnAsTrueOrFalse(edsd, "Comorbidity Chronic Neurological", COVID19_COMORBIDITY_CONDITION,
// 		    COVID19_COMORBIDITY_CHRONIC_NEUROLOGICAL);
// 		addColumnAsTrueOrFalse(edsd, "Comorbidity Current Smoker", COVID19_COMORBIDITY_CONDITION,
// 		    COVID19_COMORBIDITY_CURRENT_SMOKER);
// 		addColumnAsTrueOrFalse(edsd, "Comorbidity Diabetes", COVID19_COMORBIDITY_CONDITION, COVID19_COMORBIDITY_DIABETES);
// 		addColumnAsTrueOrFalse(edsd, "Comorbidity Former Smoker", COVID19_COMORBIDITY_CONDITION,
// 		    COVID19_COMORBIDITY_FORMER_SMOKER);
// 		addColumnAsTrueOrFalse(edsd, "Comorbidity HIV/AIDS", COVID19_COMORBIDITY_CONDITION, COVID19_COMORBIDITY_HIV_AIDS);
// 		addColumnAsTrueOrFalse(edsd, "Comorbidity Hypertension", COVID19_COMORBIDITY_CONDITION,
// 		    COVID19_COMORBIDITY_HYPERTENSION);
// 		addColumnAsTrueOrFalse(edsd, "Comorbidity Immunodeficiency", COVID19_COMORBIDITY_CONDITION,
// 		    COVID19_COMORBIDITY_IMMUNODEFICIENCY);
// 		addColumnAsTrueOrFalse(edsd, "Comorbidity Liver", COVID19_COMORBIDITY_CONDITION, COVID19_COMORBIDITY_LIVER);
// 		addColumnAsTrueOrFalse(edsd, "Comorbidity Malignancy", COVID19_COMORBIDITY_CONDITION, COVID19_COMORBIDITY_MALIGNANCY);
// 		addColumnAsTrueOrFalse(edsd, "Comorbidity Renal", COVID19_COMORBIDITY_CONDITION, COVID19_COMORBIDITY_RENAL);
// 		addColumnAsTrueOrFalse(edsd, "Comorbidity TB", COVID19_COMORBIDITY_CONDITION, COVID19_COMORBIDITY_TB);
// 		addColumnAsTrueOrFalse(edsd, "Comorbidity Other", COVID19_COMORBIDITY_CONDITION, COVID19_COMORBIDITY_OTHER);
// 		edsd.addColumn("Covid Test", edl.getObsValue(COVID19_COVID_TEST), "", new ObsValueConverter());
// 		edsd.addColumn("Order Date", edl.getObsValue(COVID19_ORDER_DATE), "", new ObsValueConverter());
// 		addColumnAsTrueOrFalse(edsd, "Rapid Antigen Test", COVID19_COVID_TEST, COVID19_RAPID_ANTIGEN_TEST);
// 		addColumnAsTrueOrFalse(edsd, "Diagnostic_PCR_Test", COVID19_COVID_TEST, COVID19_DIAGNOSTIC_PCR_TEST);
// 		edsd.addColumn("Result Date", edl.getObsValue(COVID19_RESULT_DATE), "", new ObsValueConverter());
// 		edsd.addColumn("Rapid Antigen Result", edl.getObsValue(COVID19_RAPID_ANTIGEN_RESULT), "", new ObsValueConverter());
// 		edsd.addColumn("Diagnostic_PCR_Result", edl.getObsValue(COVID19_DIAGNOSTIC_PCR_RESULT), "", new ObsValueConverter());
// 		edsd.addColumn("Patient Outcome", edl.getObsValue(COVID19_PATIENT_OUTCOME), "", new ObsValueConverter());
// 		edsd.addColumn("Date Recovered", edl.getObsValue(COVID19_DATE_RECOVERED), "", new ObsValueConverter());
// 		edsd.addColumn("Date Died", edl.getObsValue(COVID19_DATE_DIED), "", new ObsValueConverter());
// 		edsd.addColumn("Long COVID Description", edl.getObsValue(COVID19_LONG_COVID_DESCRIPTION), "",
// 		    new ObsValueConverter());
// 	}

// 	private void addColumnAsTrueOrFalse(EncounterDataSetDefinition edsd, String name, String questionConcept,
// 	        String answerConcept) {
// 		edsd.addColumn(name, edl.getObsValue(questionConcept, answerConcept), "", odc.getObsValueCodedExistsConverter());
// 	}

// 	private Mapped<? extends EncounterQuery> getEncounterQuery() {
// 		CompositionEncounterQuery ceq = new CompositionEncounterQuery();
// 		ceq.addSearch("positiveRdt", getEncounterQuery(COVID19_RAPID_ANTIGEN_RESULT));
// 		ceq.addSearch("positivePcr", getEncounterQuery(COVID19_DIAGNOSTIC_PCR_RESULT));
// 		ceq.setCompositionString("positiveRdt or positivePcr");

// 		return Mapped.mapStraightThrough(ceq);
// 	}

// 	private Mapped<? extends EncounterQuery> getEncounterQuery(String conceptUuid) {
// 		CodedObsForEncounterQuery cofeq = new CodedObsForEncounterQuery();
// 		cofeq.setQuestion(conceptService.getConceptByUuid(conceptUuid));
// 		cofeq.addConceptToInclude(conceptService.getConceptByUuid(POSITIVE));

// 		return Mapped.mapStraightThrough(cofeq);
// 	}

// 	@Override
// 	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
// 		ReportDesign design = ReportManagerUtil.createCsvReportDesign(COVID19_REPORT_DESIGN_UUID, reportDefinition);

// 		return Arrays.asList(design);
// 	}

// 	@Override
// 	public List<ReportRequest> constructScheduledRequests(ReportDefinition reportDefinition) {
// 		return null;
// 	}

// 	@Override
// 	public String getVersion() {
// 		return "1.0.0-SNAPSHOT";
// 	}
// }
