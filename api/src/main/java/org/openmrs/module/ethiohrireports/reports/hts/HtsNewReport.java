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

// import static org.openmrs.module.ohrireports.OHRIReportsConstants.ART_START_DATE;
// import static org.openmrs.module.ohrireports.OHRIReportsConstants.HTS_FOLLOW_UP_ENCOUNTER_TYPE;
// import static org.openmrs.module.ohrireports.OHRIReportsConstants.HTS_FOLLOW_UP_REPORT_UUID;

// import java.util.Arrays;
// import java.util.Date;
// import java.util.List;

// import org.openmrs.Concept;
// import org.openmrs.api.ConceptService;
// import org.openmrs.api.EncounterService;
// import org.openmrs.module.ohrireports.cohorts.definition.DateObsValueBetweenCohortDefinition;
// import org.openmrs.module.ohrireports.reports.library.EncounterDataLibrary;
// import org.openmrs.module.ohrireports.reports.library.PatientDataLibrary;
// import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
// import org.openmrs.module.reporting.data.converter.AgeConverter;
// import org.openmrs.module.reporting.data.converter.ObsValueConverter;
// import org.openmrs.module.reporting.data.encounter.definition.EncounterDatetimeDataDefinition;
// import org.openmrs.module.reporting.data.encounter.definition.EncounterTypeDataDefinition;
// import org.openmrs.module.reporting.dataset.definition.EncounterDataSetDefinition;
// import org.openmrs.module.reporting.evaluation.parameter.Mapped;
// import org.openmrs.module.reporting.evaluation.parameter.Parameter;
// import org.openmrs.module.reporting.evaluation.parameter.Parameterizable;
// import org.openmrs.module.reporting.evaluation.parameter.ParameterizableUtil;
// import org.openmrs.module.reporting.report.ReportDesign;
// import org.openmrs.module.reporting.report.ReportRequest;
// import org.openmrs.module.reporting.report.definition.ReportDefinition;
// import org.openmrs.module.reporting.report.manager.ReportManager;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;

// @Component
// public class HtsNewReport implements ReportManager {

// 	@Autowired
// 	EncounterService encounterService;

// 	@Autowired
// 	ConceptService conceptService;

// 	@Autowired
// 	PatientDataLibrary hpdl;

// 	@Autowired
// 	EncounterDataLibrary hedl;

// 	@Override
// 	public String getUuid() {
// 		return HTS_FOLLOW_UP_REPORT_UUID;
// 	}

// 	@Override
// 	public String getName() {
// 		return "HTS Follow Up Report";
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

// 	}

// 	@Override
// 	public ReportDefinition constructReportDefinition() {
// 		ReportDefinition reportDefinition = new ReportDefinition();
// 		reportDefinition.setUuid(getUuid());
// 		reportDefinition.setName(getName());
// 		reportDefinition.setDescription(getDescription());
// 		reportDefinition.setParameters(getParameters());

// 		// EncounterCohortDefinition ecd = new EncounterCohortDefinition();
// 		// ecd.addEncounterType(encounterService.getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE));

// 		reportDefinition.setBaseCohortDefinition(map(startedArtFromTransferringFacilityOnDate(),
// 		    "onOrAfter=${startDateGC},onOrBefore=${endDateGC}"));
// 		EncounterDataSetDefinition edsd = new EncounterDataSetDefinition();

// 		edsd.addParameters(getParameters());

// 		addColumns(edsd);

// 		reportDefinition.addDataSetDefinition("HTS", Mapped.mapStraightThrough(edsd));
// 		return reportDefinition;
// 	}

// 	public static <T extends Parameterizable> Mapped<T> map(T parameterizable, String mappings) {
// 		if (parameterizable == null) {
// 			throw new IllegalArgumentException("Parameterizable cannot be null");
// 		}
// 		if (mappings == null) {
// 			mappings = ""; // probably not necessary, just to be safe
// 		}
// 		return new Mapped<T>(parameterizable, ParameterizableUtil.createParameterMappings(mappings));
// 	}

// 	private void addColumns(EncounterDataSetDefinition edsd) {
// 		edsd.addColumn("Name", hpdl.getName(), "");
// 		edsd.addColumn("Age", hpdl.getAge(), "", new AgeConverter());
// 		edsd.addColumn("Sex", hpdl.getGender(), "");
// 		//edsd.addColumn("Follow Up Date ", getEncounterDate(), "");
// 		edsd.addColumn("ART Start Date ", hedl.getObsValue(ART_START_DATE), "", new ObsValueConverter());
// 		edsd.addColumn("Regimen", hedl.getObsValue("6d7d0327-e1f8-4246-bfe5-be1e82d94b14"), "", new ObsValueConverter());
// 		//edsd.addColumn("Status", hedl.getObsValue(REASON_FOR_ART_ELIGABLITY), "", new ObsValueConverter());
// 		edsd.addColumn("Encounter type", getEncounterTypeNamDefinition(), "");

// 	}

// 	public CohortDefinition startedArtFromTransferringFacilityOnDate() {
// 		Concept starteArtFromTransferringFacility = conceptService.getConceptByUuid(ART_START_DATE);
// 		DateObsValueBetweenCohortDefinition cd = new DateObsValueBetweenCohortDefinition();
// 		cd.addEncounterType(encounterService.getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE));
// 		cd.setName("Patients Who Started ART From the Transferring Facility between date");
// 		cd.setQuestion(starteArtFromTransferringFacility);
// 		cd.addParameters(getParameters());
// 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
// 		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));

// 		return cd;

// 	}

// 	public EncounterDatetimeDataDefinition getEncounterDate() {

// 		return new EncounterDatetimeDataDefinition();
// 	}

// 	public EncounterTypeDataDefinition getEncounterTypeNamDefinition() {

// 		return new EncounterTypeDataDefinition();
// 	}

// 	@Override
// 	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
// 		// ReportDesign design = ReportManagerUtil.createCsvReportDesign(HTS_REPORT_DESIGN_UUID, reportDefinition);

// 		// return Arrays.asList(design);
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
