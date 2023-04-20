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

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

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
	
	public final static String TODAYZ_APPOINTMENTS = "ccbcf6d8-77b7-44a5-bb43-d352478ea4e9";
	
	public final static String CLIENTS_WITHOUT_COVID_19_OUTCOMES = "db6c4a18-28c6-423c-9da0-58d19e364a7f";
	
	public final static String COVID_CLIENTS_WITH_PENDING_LAB_RESULTS = "166aa2b1-ce55-4d16-9643-ca9d2e2694ea";
	
	public final static String ALL_PATIENTS_COHORT_UUID = "895d0025-84e2-4306-bdd9-66acc150ec21";
	
	public final static String MRN_PATIENT_IDENTIFIERS = "52c28db7-09fb-4d33-8f9f-4500347256b6";
	
	public final static String OPENMRS_PATIENT_IDENTIFIERS = "05a29f94-c0ed-11e2-94be-8c13b969e334";
	
	public final static String CURRENTLY_BREAST_FEEDING_CHILD = "5632AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String YES = "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String NO = "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String UNKNOWN = "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
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
	
	public final static String ART_START_DATE = "159599AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String TRANSFERRED_IN = "160563AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String REASON_FOR_ART_ELIGIBILITY = "162225AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String FOLLOW_UP_DATE = "163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String REGIMEN = "6d7d0327-e1f8-4246-bfe5-be1e82d94b14";
	
	public final static String ARV_DISPENSED_IN_DAYS = "3a0709e9-d7a8-44b9-9512-111db5ce3989";
	
	public final static String TREATMENT_END_DATE = "164384AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	public final static String TB_SCREENING_DATE = "179497a0-6f07-469f-bb2e-9b85644a82af";
	
	public final static String TB_DIAGNOSTIC_TEST_RESULT = "c20140f7-d45d-4b44-a1b9-0534861a615d";
	
	public final static String DIAGNOSTIC_TEST = "002240c0-8672-4631-a32d-9bb9c34e4665";
	
	public final static String SMEAR_ONLY = "3a6f3d9d-623c-452b-8f96-bbdb8805aa97";
	
	public final static String MTB_RIF_ASSAY_WITH_OTHER_TESTING = "8fc383d0-7739-40ba-91a9-d0351c533284";
	
	public final static String MTB_RIF_ASSAY_WITH_OTHEROUT_TESTING = "d89ac55a-6c83-458b-a31d-2a28965955d5";
	
	public final static String LF_LAM_MTB_RIF = "9f4d51da-b09f-4de7-ac55-678f700eadfd";
	
	public final static String LF_LAM = "34e4571e-7950-42e8-9936-a204f5f01a5b";
	
	public final static String ADDITIONAL_TEST_OTHERTHAN_GENE_XPERT = "9224c3c7-d2d7-4165-88cb-81e5aec30d70";
	
	public final static String SPECIMEN_SENT = "161934AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

	public final static String CXCA_TREATMENT_TYPE= "3a8bb4b4-7496-415d-a327-57ae3711d4eb";

	public final static String CXCA_TREATMENT_TYPE_NO_TREATMENT= "1107AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

	public final static String CXCA_TREATMENT_TYPE_CRYOTHERAPY= "162812AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

	public final static String CXCA_TREATMENT_TYPE_THERMOCOAGULATION= "166706AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

	public final static String CXCA_TREATMENT_TYPE_LEEP= "165084AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

	public final static String CXCA_TREATMENT_TYPE_OTHER_TREATMENT= "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

	public final static String CXCA_TREATMENT_STARTING_DATE="163526AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

	public final static String CXCA_TYPE_OF_SCREENING="9bd94bd2-059c-4273-8b6b-31a8e02c6f02";

	public final static String CXCA_TYPE_OF_SCREENING_FIRST_TIME="3a8bb4b4-7496-415d-a327-57ae3711d4eb";

	public final static String CXCA_TYPE_OF_SCREENING_RESCREENING="13c3ee77-4e7c-4224-ae40-0b2727932a0f";

	public final static String CXCA_TYPE_OF_SCREENING_POST_TREATMENT="3f4a6148-39c1-4980-81c6-6d703367c4a6";

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
	
	//#region Report Group 
	public final static String DATIM_REPORT = "DATIM";
	
	public final static String HISM_REPORT = "HISM";
	
	public final static String LINE_LIST_REPORT = "LINELIST";
	//#endregion
}
