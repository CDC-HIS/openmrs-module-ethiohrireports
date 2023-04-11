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

// import java.util.Arrays;
// import java.util.Date;
// import java.util.List;

// import org.openmrs.api.context.Context;
// import org.openmrs.module.ohrireports.reports.datasetdefinition.HtsNewDataSetDefinition;
// import org.openmrs.module.ohrireports.reports.datasetdefinition.HtsNewDataSetDefinitionAdx;
// import org.openmrs.module.reporting.evaluation.parameter.Mapped;
// import org.openmrs.module.reporting.evaluation.parameter.Parameter;
// import org.openmrs.module.reporting.evaluation.parameter.Parameterizable;
// import org.openmrs.module.reporting.evaluation.parameter.ParameterizableUtil;
// import org.openmrs.module.reporting.report.ReportDesign;
// import org.openmrs.module.reporting.report.ReportRequest;
// import org.openmrs.module.reporting.report.definition.ReportDefinition;
// import org.openmrs.module.reporting.report.manager.ReportManager;
// import org.springframework.stereotype.Component;
// import static org.openmrs.module.ohrireports.OHRIReportsConstants.HTS_FOLLOW_UP_ENCOUNTER_TYPE;

// @Component
// public class TX_New_ReportAdx implements ReportManager {

// 	@Override
// 	public String getUuid() {
// 		return "5f1e54f3-d171-4a41-861c-304f240d618b";
// 	}

// 	@Override
// 	public String getName() {
// 		return "TX New ADX";
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

// 		HtsNewDataSetDefinitionAdx htsNewDataSetDefinition = new HtsNewDataSetDefinitionAdx();
// 		htsNewDataSetDefinition.addParameters(getParameters());
// 		htsNewDataSetDefinition.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid(
// 		    HTS_FOLLOW_UP_ENCOUNTER_TYPE));
// 		reportDefinition.addDataSetDefinition("TX-New Aggregate by Age and Gender",
// 		    map(htsNewDataSetDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
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

// 	@Override
// 	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
// 		// ReportDesign design =
// 		// ReportManagerUtil.createCsvReportDesign(HTS_REPORT_DESIGN_UUID,
// 		// reportDefinition);

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
