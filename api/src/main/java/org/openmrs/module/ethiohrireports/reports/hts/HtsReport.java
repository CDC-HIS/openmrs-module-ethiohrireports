// /**
//  * This Source Code Form is subject to the terms of the Mozilla Public License,
//  * v. 2.0. If a copy of the MPL was not distributed with this file, You can
//  * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
//  * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
//  * <p>
//  * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
//  * graphic logo is a trademark of OpenMRS Inc.
//  */
// package org.openmrs.module.ohrireports.reports.hts;

// import static org.openmrs.module.ohrireports.OHRIReportsConstants.APPROACH;
// import static org.openmrs.module.ohrireports.OHRIReportsConstants.CONFIRMATORY_HIV_TEST_RESULT;
// import static org.openmrs.module.ohrireports.OHRIReportsConstants.DATE_CLIENT_RECEIVED_FINAL_RESULT;
// import static org.openmrs.module.ohrireports.OHRIReportsConstants.FINAL_HIV_RESULT;
// import static org.openmrs.module.ohrireports.OHRIReportsConstants.HTS_ENCOUNTER_TYPE;
// import static org.openmrs.module.ohrireports.OHRIReportsConstants.HTS_REPORT_UUID;
// import static org.openmrs.module.ohrireports.OHRIReportsConstants.HTS_RETROSPECTIVE_ENCOUNTER_TYPE;
// import static org.openmrs.module.ohrireports.OHRIReportsConstants.INITIAL_HIV_TEST_RESULT;
// import static org.openmrs.module.ohrireports.OHRIReportsConstants.LINKED_TO_CARE_AND_TREATMENT_IN_THIS_FACILITY;
// import static org.openmrs.module.ohrireports.OHRIReportsConstants.POPULATION_TYPE;
// import static org.openmrs.module.ohrireports.OHRIReportsConstants.SETTING_OF_HIV_TEST;

// import java.util.Arrays;
// import java.util.Date;
// import java.util.List;

// import org.openmrs.api.EncounterService;
// import org.openmrs.module.ohrireports.reports.library.EncounterDataLibrary;
// import org.openmrs.module.ohrireports.reports.library.PatientDataLibrary;
// import org.openmrs.module.reporting.cohort.definition.EncounterCohortDefinition;
// import org.openmrs.module.reporting.common.ObjectUtil;
// import org.openmrs.module.reporting.data.converter.AgeConverter;
// import org.openmrs.module.reporting.data.converter.ObsValueConverter;
// import org.openmrs.module.reporting.dataset.definition.EncounterDataSetDefinition;
// import org.openmrs.module.reporting.evaluation.parameter.Mapped;
// import org.openmrs.module.reporting.evaluation.parameter.Parameter;
// import org.openmrs.module.reporting.query.encounter.definition.BasicEncounterQuery;
// import org.openmrs.module.reporting.query.encounter.definition.EncounterQuery;
// import org.openmrs.module.reporting.query.encounter.definition.MappedParametersEncounterQuery;
// import org.openmrs.module.reporting.report.ReportDesign;
// import org.openmrs.module.reporting.report.ReportRequest;
// import org.openmrs.module.reporting.report.definition.ReportDefinition;
// import org.openmrs.module.reporting.report.manager.ReportManager;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;

// @Component
// public class HtsReport implements ReportManager {

// 	@Autowired
// 	EncounterService encounterService;

// 	@Autowired
// 	PatientDataLibrary hpdl;

// 	@Autowired
// 	EncounterDataLibrary hedl;

// 	@Override
// 	public String getUuid() {
// 		return HTS_REPORT_UUID;
// 	}

// 	@Override
// 	public String getName() {
// 		return "HTS Report";
// 	}

// 	@Override
// 	public String getDescription() {
// 		return null;
// 	}

// 	@Override
// 	public List<Parameter> getParameters() {
// 		Parameter startDate = new Parameter("startDate", "Start Date", Date.class);
// 		startDate.setRequired(false);
// 		Parameter startDateGC = new Parameter("startDateGC", " ", Date.class);
// 		startDateGC.setRequired(false);
// 		Parameter endDate = new Parameter("endDate", "End Date", Date.class);
// 		endDate.setRequired(false);
// 		Parameter endDateGC = new Parameter("endDateGC", " ", Date.class);
// 		endDateGC.setRequired(false);
// 		return Arrays.asList(startDate, startDateGC, endDate, endDateGC);
// 		// return Arrays.asList(new Parameter("startDate", "Start Date", Date.class), new Parameter("startDateGC",
// 		//         "Start Date GC", Date.class), new Parameter("endDate", "End Date", Date.class), new Parameter("endDateGC",
// 		//         "End Date GC", Date.class));
// 	}

// 	@Override
// 	public ReportDefinition constructReportDefinition() {
// 		ReportDefinition reportDefinition = new ReportDefinition();
// 		reportDefinition.setUuid(getUuid());
// 		reportDefinition.setName(getName());
// 		reportDefinition.setDescription(getDescription());

// 		reportDefinition.setParameters(getParameters());

// 		EncounterCohortDefinition ecd = new EncounterCohortDefinition();
// 		ecd.addEncounterType(encounterService.getEncounterTypeByUuid(HTS_ENCOUNTER_TYPE));
// 		ecd.addEncounterType(encounterService.getEncounterTypeByUuid(HTS_RETROSPECTIVE_ENCOUNTER_TYPE));
// 		reportDefinition.setBaseCohortDefinition(Mapped.mapStraightThrough(ecd));

// 		EncounterDataSetDefinition edsd = new EncounterDataSetDefinition();
// 		edsd.addParameters(getParameters());
// 		edsd.addRowFilter(getEncounterQuery());
// 		addColumns(edsd);

// 		reportDefinition.addDataSetDefinition("HTS", Mapped.mapStraightThrough(edsd));
// 		return reportDefinition;
// 	}

// 	private void addColumns(EncounterDataSetDefinition edsd) {
// 		edsd.addColumn("Name", hpdl.getName(), "");
// 		edsd.addColumn("Age", hpdl.getAge(), "", new AgeConverter());
// 		edsd.addColumn("Sex", hpdl.getGender(), "");
// 		edsd.addColumn("Setting of HIV test", hedl.getObsValue(SETTING_OF_HIV_TEST), "", new ObsValueConverter());
// 		edsd.addColumn("Approach", hedl.getObsValue(APPROACH), "", new ObsValueConverter());
// 		edsd.addColumn("Population type", hedl.getObsValue(POPULATION_TYPE), "", new ObsValueConverter());
// 		edsd.addColumn("Initial HIV Test result", hedl.getObsValue(INITIAL_HIV_TEST_RESULT), "", new ObsValueConverter());
// 		edsd.addColumn("Confirmatory HIV test result", hedl.getObsValue(CONFIRMATORY_HIV_TEST_RESULT), "",
// 		    new ObsValueConverter());
// 		edsd.addColumn("Final HIV Result", hedl.getObsValue(FINAL_HIV_RESULT), "", new ObsValueConverter());
// 		edsd.addColumn("Date client received final result", hedl.getObsValue(DATE_CLIENT_RECEIVED_FINAL_RESULT), "",
// 		    new ObsValueConverter());
// 		edsd.addColumn("Linked to care and treatment in this facility",
// 		    hedl.getObsValue(LINKED_TO_CARE_AND_TREATMENT_IN_THIS_FACILITY), "", new ObsValueConverter());
// 	}

// 	private Mapped<? extends EncounterQuery> getEncounterQuery() {
// 		BasicEncounterQuery encounterQuery = new BasicEncounterQuery();
// 		encounterQuery.addEncounterType(encounterService.getEncounterTypeByUuid(HTS_ENCOUNTER_TYPE));
// 		encounterQuery.addEncounterType(encounterService.getEncounterTypeByUuid(HTS_RETROSPECTIVE_ENCOUNTER_TYPE));
// 		encounterQuery.addParameter(new Parameter("onOrAfter", "On Or After", Date.class));
// 		encounterQuery.addParameter(new Parameter("onOrBefore", "On Or Before", Date.class));
// 		MappedParametersEncounterQuery q = new MappedParametersEncounterQuery(encounterQuery,
// 		        ObjectUtil.toMap("onOrAfter=${startDateGC},onOrBefore=${endDateGC}"));
// 		return Mapped.mapStraightThrough(q);
// 	}

// 	@Override
// 	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
// 		//ReportDesign design = ReportManagerUtil.createCsvReportDesign(HTS_REPORT_DESIGN_UUID, reportDefinition);

// 		return null;
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
