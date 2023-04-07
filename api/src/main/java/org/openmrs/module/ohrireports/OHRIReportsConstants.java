/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ohrireports;

public class OHRIReportsConstants {
	
	/**
	 * Encounter types
	 */
	public final static String COVID_ASSESSMENT_ENCOUNTER_TYPE = "253a43d3-c99e-415c-8b78-ee7d4d3c1d54";
	
	public final static String CARE_AND_TREATMENT_SERVICE_ENROLLMENT_ENCOUNTER_TYPE = "7e54cd64-f9c3-11eb-8e6a-57478ce139b0";
	
	public final static String HTS_ENCOUNTER_TYPE = "30b849bd-c4f4-4254-a033-fe9cf01001d8";
	
	public final static String HTS_FOLLOW_UP_ENCOUNTER_TYPE = "136b2ded-22a3-4831-a39a-088d35a50ef5";
	
	public final static String COVID_VACCINATION_ENCOUNTER_TYPE = "5b37ce7a-c55e-4226-bdc8-5af04025a6de";
	
	public final static String HTS_RETROSPECTIVE_ENCOUNTER_TYPE = "79c1f50f-f77d-42e2-ad2a-d29304dde2fe";
	
	/**
	 * Cohort definitions
	 */
	public final static String CLIENTS_ASSESSED_FOR_COVID = "ec373b01-4ba3-488e-a322-9dd6a50cfdf7";
	
	public final static String CLIENTS_ENROLLED_TO_CARE = "51bec6f7-df43-426e-a83e-c1ae5501372f";
	
	public final static String HTS_CLIENTS = "7c1b4906-1caf-4a8e-a51d-7abdbb896805";
	
	public final static String CLIENTS_VACCINATED_FOR_COVID = "b5d52da9-10c2-43af-ae23-552acc5e445b";
	
	public final static String CLIENTS_WITH_COVID_OUTCOMES = "afb0d950-48fd-44d7-ae2c-79615cd125f0";
	
	public final static String COVID_CLIENTS_WITH_COLLECTED_SAMPLES = "a56b9edb-454a-4524-bc91-f5e3cdd10b6a";
	
	public final static String COVID_CLIENTS_WITH_CONFIRMED_LAB_RESULTS = "0cb7a13d-9088-4be4-9279-51190f9abd1b";
	
	public final static String COVID19_POSITIVE_CLIENTS = "1523b1e5-b6d0-44fb-bd9e-c91bfefb4d1c";
	
	public final static String TODAYZ_APPOINTMENTS = "ccbcf6d8-77b7-44a5-bb43-d352478ea4e9";
	
	public final static String CLIENTS_WITHOUT_COVID_19_OUTCOMES = "db6c4a18-28c6-423c-9da0-58d19e364a7f";
	
	public final static String COVID_CLIENTS_WITH_PENDING_LAB_RESULTS = "166aa2b1-ce55-4d16-9643-ca9d2e2694ea";
	
	public final static String ALL_PATIENTS_COHORT_UUID = "895d0025-84e2-4306-bdd9-66acc150ec21";
	
	public final static String MRN_PATIENT_IDENTIFIERS = "52c28db7-09fb-4d33-8f9f-4500347256b6";
	
	public final static String OPENMRS_PATIENT_IDENTIFIERS = "05a29f94-c0ed-11e2-94be-8c13b969e334";
	
	public final static String CURRENTLY_BREAST_FEEDING_CHILD = "5632AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String BREAST_FEEDING_YES = "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	/**
	 * Associated Concepts
	 */
	public final static String VACCINATION_DATE = "1410AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID_TREATMENT_OUTCOME = "a845f3e6-4432-4de4-9fff-37fa270b1a06";
	
	public final static String SPECIMEN_COLLECTION_DATE = "159951AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID_LAB_TEST_CONFIRMATION_DATE = "a51c05e1-5ad5-420d-a082-966d2989b716";
	
	public final static String FINAL_COVID19_TEST_RESULT = "5da5c21b-969f-41bd-9091-e40d4c707544";
	
	public final static String RETURN_VISIT_DATE = "5096AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	//public final static String POSITIVE = "6378487b-584d-4422-a6a6-56c8830873ff";
	public final static String POSITIVE = "703AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String NEGATIVE = "664AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String SETTING_OF_HIV_TEST = "13abe5c9-6de2-4970-b348-36d352ee8eeb";
	
	public final static String APPROACH = "9641ead9-8821-4898-b633-a8e96c0933cf";
	
	public final static String POPULATION_TYPE = "166432AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String INITIAL_HIV_TEST_RESULT = "e767ba5d-7560-43ba-a746-2b0ff0a2a513";
	
	public final static String CONFIRMATORY_HIV_TEST_RESULT = "dbc4f8e9-7098-4585-9509-e2f84a4d8c6e";
	
	public final static String FINAL_HIV_RESULT = "e16b0068-b6a2-46b7-aba9-e3be00a7b4ab";
	
	public final static String DATE_CLIENT_RECEIVED_FINAL_RESULT = "160082AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String LINKED_TO_CARE_AND_TREATMENT_IN_THIS_FACILITY = "e8e8fe71-adbb-48e7-b531-589985094d30";
	
	public final static String COVID19_ASSESSMENT_DATE = "160753AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_ASSESSMENT_REASON = "ae46f4b1-c15d-4bba-ab41-b9157b82b0ce";
	
	public final static String COVID19_ASSESSMENT_CONTACT_CASE = "5340f478-ec5d-41e6-bc62-961c52014d4d";
	
	public final static String COVID19_ASSESSMENT_ENTRY_COUNTRY = "677f4d21-7293-4810-abe6-57a192acde57";
	
	public final static String COVID19_ASSESSMENT_FOLLOW_UP = "ad8be6bf-ced7-4390-a6af-c5acebeac216";
	
	public final static String COVID19_ASSESSMENT_HEALTH_WORKER = "30320fb8-b29b-443f-98cf-f3ef491f8947";
	
	public final static String COVID19_ASSESSMENT_FRONTLINE_WORKER = "38769c82-a3d3-4714-97b7-015726cdb209";
	
	public final static String COVID19_ASSESSMENT_POST_MORTEM = "1cee0ab3-bf06-49e9-a49c-baf261620c67";
	
	public final static String COVID19_ASSESSMENT_QUARANTINE = "e0f1584a-cc8b-48e9-980f-64d9f724caf8";
	
	public final static String COVID19_ASSESSMENT_RDT_CONFIRMATORY = "0ed2e3ca-b9a6-4ff6-ac74-4d4cd9520acc";
	
	public final static String COVID19_ASSESSMENT_SURVEILLANCE = "8a6ab892-1b1d-4ad9-82da-c6c38ee8fcfb";
	
	public final static String COVID19_ASSESSMENT_SYMPTOMATIC = "1068AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_ASSESSMENT_TRAVEL_OUT_COUNTRY = "f8c9c2cc-3070-444e-8818-26fb8100bb78";
	
	public final static String COVID19_ASSESSMENT_VOLUNTARY = "f974e267-feeb-42be-9d37-61554dad16b1";
	
	public final static String COVID19_ASSESSMENT_OTHER = "a782ce41-5499-4521-ae42-16347fe6b9e0";
	
	public final static String COVID19_ASSESSMENT_PRESENTATION = "de3bc9b7-05b5-41b6-a38d-8d2eec646c4f";
	
	public final static String COVID19_DATE_ONSET_SYMPTOMS = "1730AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_PRESENTING_SYMPTOMS = "244b0dc0-eb1b-46ae-b62a-f580345d4f46";
	
	public final static String COVID19_SYMPTOM_ABDOMINAL_PAIN = "151AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_SYMPTOM_CHEST_PAIN = "120749AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_SYMPTOM_COUGH = "feb2a008-403c-480e-9d0e-f7100b370815";
	
	public final static String COVID19_SYMPTOM_DIARRHOEA = "6e7d9cd7-8a0b-40ca-bf57-c4fc7510b09a";
	
	public final static String COVID19_SYMPTOM_DISTURBANCE_CONSCIOUSNESS = "118876AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_SYMPTOM_GENERAL_WEAKNESS = "5226AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_SYMPTOM_HEADACHE = "139084AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_SYMPTOM_FEVER_CHILLS = "140237AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_SYMPTOM_IRRITABILITY_CONFUSION = "120345AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_SYMPTOM_JOINT_PAIN = "116558AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_SYMPTOM_LOSS_SMELL = "135589AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_SYMPTOM_LOSS_TASTE = "135588AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_SYMPTOM_MUSCULAR_PAIN = "133632AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_SYMPTOM_NAUSEA_VOMITING = "5978AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_SYMPTOM_RED_EYES = "127777AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_SYMPTOM_RUNNY_NOSE = "94a6ae53-0c36-4889-b2b7-025e48542835";
	
	public final static String COVID19_SYMPTOM_SHORTNESS_BREATH = "141600AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_SYMPTOM_SNEEZING = "126319AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String CgetMRNOVID19_SYMPTOM_SORE_THROAT = "158843AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_SYMPTOM_TIREDNESS = "124628AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_SYMPTOM_OTHER = "08287bbb-3b2d-41fc-83e6-9c827e79b8e0";
	
	public final static String COVID19_COMORBIDITY_PRESENT = "166020AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_COMORBIDITY_CONDITION = "0651869c-6e90-48d6-b25c-406270c76bee";
	
	public final static String COVID19_COMORBIDITY_CARDIOVASCULAR = "9a40049b-4875-4a1f-8969-d9231b807ec7";
	
	public final static String COVID19_COMORBIDITY_CHRONIC_LUNG = "145270AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_COMORBIDITY_CHRONIC_NEUROLOGICAL = "165646AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_COMORBIDITY_CURRENT_SMOKER = "155600AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_COMORBIDITY_DIABETES = "e3ea3c4b-df5f-4659-b357-93e3de325660";
	
	public final static String COVID19_COMORBIDITY_FORMER_SMOKER = "156358AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_COMORBIDITY_HIV_AIDS = "746d0622-21d3-4c57-8ee9-4e2a69637d8c";
	
	public final static String COVID19_COMORBIDITY_HYPERTENSION = "117399AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_COMORBIDITY_IMMUNODEFICIENCY = "156753AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_COMORBIDITY_LIVER = "139201AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_COMORBIDITY_MALIGNANCY = "c4879004-17d7-4eee-b5fc-942e855e2027";
	
	public final static String COVID19_COMORBIDITY_RENAL = "9c37b227-6426-4866-9fb9-a19800572924";
	
	public final static String COVID19_COMORBIDITY_TB = "1a0ce98b-3fc0-4a07-b813-9dad49d8347e";
	
	public final static String COVID19_COMORBIDITY_OTHER = "166104AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_COVID_TEST = "069f6dfe-88c1-4a45-a894-0d99549c8718";
	
	public final static String COVID19_ORDER_DATE = "162078AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_RAPID_ANTIGEN_TEST = "165852AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_DIAGNOSTIC_PCR_TEST = "165840AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_RESULT_DATE = "163724AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_RAPID_ANTIGEN_RESULT = "cbcbb029-f11f-4437-9d53-1d0f0a170433";
	
	public final static String COVID19_DIAGNOSTIC_PCR_RESULT = "3f4ee14b-b4ab-4597-9fe9-406883b63d76";
	
	public final static String COVID19_PATIENT_OUTCOME = "a845f3e6-4432-4de4-9fff-37fa270b1a06";
	
	public final static String COVID19_DATE_RECOVERED = "162868AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_DATE_DIED = "1543AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String COVID19_LONG_COVID_DESCRIPTION = "4560c560-4bd6-4989-bff8-6e4e1fa39972";
	
	public final static String ART_START_DATE = "159599AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String TRANSFERRED_IN = "160563AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String REASON_FOR_ART_ELIGIBILITY = "162225AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String FOLLOW_UP_DATE = "163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String REGIMEN = "6d7d0327-e1f8-4246-bfe5-be1e82d94b14";
	
	public final static String ARV_DISPENSED_IN_DAYS = "3a0709e9-d7a8-44b9-9512-111db5ce3989";
	
	public final static String TREATMENT_END_DATE = "164384AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	/**
	 * Reports
	 */
	public final static String HTS_REPORT_UUID = "3ffa5a53-fc65-4a1e-a434-46dbcf1c2de2";
	
	public final static String HTS_FOLLOW_UP_REPORT_UUID = "136b2ded-22a3-4831-a39a-088d35a50ef5";
	
	public final static String HTS_REPORT_DESIGN_UUID = "13aae526-a565-489f-b529-b1d96cca5f7c";
	
	public final static String COVID19_REPORT_UUID = "ecabd559-14f6-4c65-87af-1254dfdf1304";
	
	public final static String COVID19_REPORT_DESIGN_UUID = "4e33bb15-ac1c-4e82-a863-77cb705c6512";
	
	public final static String PATIENT_STATUS = "160433AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String TRANSFERRED_UUID = "1693AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String ALIVE = "160429AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String RESTART = "ee957295-85b9-4433-bf12-45886cdc7dd1";
	
	//#region
	public final static String UNIQUE_ANTIRETROVAIRAL_THERAPY_UAN = "164402AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String SERVICE_DELIVERY_POINT_NUMBER_MRN = "162054AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

	public final static String TB_TREATMENT_START_DATE = "1113AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	//TODO: update as soon as the concept created 
	public final static String ADHERENCE_UUID = "";
	//#endregion
	
}
