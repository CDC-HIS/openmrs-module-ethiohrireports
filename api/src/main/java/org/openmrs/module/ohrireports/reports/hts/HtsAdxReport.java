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

// import java.io.File;
// import java.io.IOException;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.List;

// import org.apache.commons.logging.Log;
// import org.apache.commons.logging.LogFactory;
// import org.openmrs.module.ohrireports.reports.library.HtsIndicatorLibrary;
// import org.openmrs.module.ohrireports.reports.renderer.AdxTemplateRenderer;
// import org.openmrs.module.reporting.cohort.definition.AllPatientsCohortDefinition;
// import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
// import org.openmrs.module.reporting.evaluation.parameter.Mapped;
// import org.openmrs.module.reporting.evaluation.parameter.Parameter;
// import org.openmrs.module.reporting.report.ReportDesign;
// import org.openmrs.module.reporting.report.ReportDesignResource;
// import org.openmrs.module.reporting.report.ReportRequest;
// import org.openmrs.module.reporting.report.definition.ReportDefinition;
// import org.openmrs.module.reporting.report.manager.ReportManager;
// import org.openmrs.module.reporting.report.util.ReportUtil;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
// import org.springframework.util.ResourceUtils;

// @Component
// public class HtsAdxReport implements ReportManager {

// 	private Log log = LogFactory.getLog(this.getClass());

// 	public static final String ADX_REPORT_RESOURCE_NAME = "adx-hiv_data.xml";

// 	@Autowired
// 	HtsIndicatorLibrary hil;

// 	@Override
// 	public String getUuid() {
// 		return "12f236b1-b0b5-4ecc-9037-681c23fb45bd";
// 	}

// 	@Override
// 	public String getName() {
// 		return "ADX-HIV Report";
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

// 		AllPatientsCohortDefinition allPatientsCohortDefinition = new AllPatientsCohortDefinition();
// 		reportDefinition.setBaseCohortDefinition(Mapped.mapStraightThrough(allPatientsCohortDefinition));

// 		CohortIndicatorDataSetDefinition cidsd = new CohortIndicatorDataSetDefinition();

// 		//POSITIVES
// 		cidsd.addColumn("lt1fp", "<1, Female, Positive", hil.getPatientsWithHtsIndicator(0, 0, 'F', '+'), "");
// 		cidsd.addColumn("lt1mp", "<1, Male, Positive", hil.getPatientsWithHtsIndicator(0, 0, 'M', '+'), "");
// 		cidsd.addColumn("1_4fp", "1-4, Female, Positive", hil.getPatientsWithHtsIndicator(1, 4, 'F', '+'), "");
// 		cidsd.addColumn("1_4mp", "1-4, Male, Positive", hil.getPatientsWithHtsIndicator(1, 4, 'M', '+'), "");
// 		cidsd.addColumn("5_9fp", "5-9, Female, Positive", hil.getPatientsWithHtsIndicator(5, 9, 'F', '+'), "");
// 		cidsd.addColumn("5_9mp", "5-9, Male, Positive", hil.getPatientsWithHtsIndicator(5, 9, 'M', '+'), "");
// 		cidsd.addColumn("10_14fp", "10-14, Female, Positive", hil.getPatientsWithHtsIndicator(10, 14, 'F', '+'), "");
// 		cidsd.addColumn("10_14mp", "10-14, Male, Positive", hil.getPatientsWithHtsIndicator(10, 14, 'M', '+'), "");
// 		cidsd.addColumn("15_19fp", "15-19, Female, Positive", hil.getPatientsWithHtsIndicator(15, 19, 'F', '+'), "");
// 		cidsd.addColumn("15_19mp", "15-19, Male, Positive", hil.getPatientsWithHtsIndicator(15, 19, 'M', '+'), "");
// 		cidsd.addColumn("20_24fp", "20-24, Female, Positive", hil.getPatientsWithHtsIndicator(20, 24, 'F', '+'), "");
// 		cidsd.addColumn("20_24mp", "20-24, Male, Positive", hil.getPatientsWithHtsIndicator(20, 24, 'M', '+'), "");
// 		cidsd.addColumn("25_29fp", "25-29, Female, Positive", hil.getPatientsWithHtsIndicator(25, 29, 'F', '+'), "");
// 		cidsd.addColumn("25_29mp", "25-29, Male, Positive", hil.getPatientsWithHtsIndicator(25, 29, 'M', '+'), "");
// 		cidsd.addColumn("30_34fp", "30-34, Female, Positive", hil.getPatientsWithHtsIndicator(30, 34, 'F', '+'), "");
// 		cidsd.addColumn("30_34mp", "30-34, Male, Positive", hil.getPatientsWithHtsIndicator(30, 34, 'M', '+'), "");
// 		cidsd.addColumn("35_39fp", "35-39, Female, Positive", hil.getPatientsWithHtsIndicator(35, 39, 'F', '+'), "");
// 		cidsd.addColumn("35_39mp", "35-39, Male, Positive", hil.getPatientsWithHtsIndicator(35, 39, 'M', '+'), "");
// 		cidsd.addColumn("40_44fp", "40-44, Female, Positive", hil.getPatientsWithHtsIndicator(40, 44, 'F', '+'), "");
// 		cidsd.addColumn("40_44mp", "40-44, Male, Positive", hil.getPatientsWithHtsIndicator(40, 44, 'M', '+'), "");
// 		cidsd.addColumn("45_49fp", "45-49, Female, Positive", hil.getPatientsWithHtsIndicator(45, 49, 'F', '+'), "");
// 		cidsd.addColumn("45_49mp", "45-49, Male, Positive", hil.getPatientsWithHtsIndicator(45, 49, 'M', '+'), "");
// 		cidsd.addColumn("gt50fp", "50+, Female, Positive", hil.getPatientsWithHtsIndicator(50, 9999, 'F', '+'), "");
// 		cidsd.addColumn("gt50mp", "50+, Male, Positive", hil.getPatientsWithHtsIndicator(50, 9999, 'M', '+'), "");

// 		//NEGATIVES
// 		cidsd.addColumn("lt1fn", "<1, Female, Negative", hil.getPatientsWithHtsIndicator(0, 0, 'F', '-'), "");
// 		cidsd.addColumn("lt1mn", "<1, Male, Negative", hil.getPatientsWithHtsIndicator(0, 0, 'M', '-'), "");
// 		cidsd.addColumn("1_4fn", "1-4, Female, Negative", hil.getPatientsWithHtsIndicator(1, 4, 'F', '-'), "");
// 		cidsd.addColumn("1_4mn", "1-4, Male, Negative", hil.getPatientsWithHtsIndicator(1, 4, 'M', '-'), "");
// 		cidsd.addColumn("5_9fn", "5-9, Female, Negative", hil.getPatientsWithHtsIndicator(5, 9, 'F', '-'), "");
// 		cidsd.addColumn("5_9mn", "5-9, Male, Negative", hil.getPatientsWithHtsIndicator(5, 9, 'M', '-'), "");
// 		cidsd.addColumn("10_14fn", "10-14, Female, Negative", hil.getPatientsWithHtsIndicator(10, 14, 'F', '-'), "");
// 		cidsd.addColumn("10_14mn", "10-14, Male, Negative", hil.getPatientsWithHtsIndicator(10, 14, 'M', '-'), "");
// 		cidsd.addColumn("15_19fn", "15-19, Female, Negative", hil.getPatientsWithHtsIndicator(15, 19, 'F', '-'), "");
// 		cidsd.addColumn("15_19mn", "15-19, Male, Negative", hil.getPatientsWithHtsIndicator(15, 19, 'M', '-'), "");
// 		cidsd.addColumn("20_24fn", "20-24, Female, Negative", hil.getPatientsWithHtsIndicator(20, 24, 'F', '-'), "");
// 		cidsd.addColumn("20_24mn", "20-24, Male, Negative", hil.getPatientsWithHtsIndicator(20, 24, 'M', '-'), "");
// 		cidsd.addColumn("25_29fn", "25-29, Female, Negative", hil.getPatientsWithHtsIndicator(25, 29, 'F', '-'), "");
// 		cidsd.addColumn("25_29mn", "25-29, Male, Negative", hil.getPatientsWithHtsIndicator(25, 29, 'M', '-'), "");
// 		cidsd.addColumn("30_34fn", "30-34, Female, Negative", hil.getPatientsWithHtsIndicator(30, 34, 'F', '-'), "");
// 		cidsd.addColumn("30_34mn", "30-34, Male, Negative", hil.getPatientsWithHtsIndicator(30, 34, 'M', '-'), "");
// 		cidsd.addColumn("35_39fn", "35-39, Female, Negative", hil.getPatientsWithHtsIndicator(35, 39, 'F', '-'), "");
// 		cidsd.addColumn("35_39mn", "35-39, Male, Negative", hil.getPatientsWithHtsIndicator(35, 39, 'M', '-'), "");
// 		cidsd.addColumn("40_44fn", "40-44, Female, Negative", hil.getPatientsWithHtsIndicator(40, 44, 'F', '-'), "");
// 		cidsd.addColumn("40_44mn", "40-44, Male, Negative", hil.getPatientsWithHtsIndicator(40, 44, 'M', '-'), "");
// 		cidsd.addColumn("45_49fn", "45-49, Female, Negative", hil.getPatientsWithHtsIndicator(45, 49, 'F', '-'), "");
// 		cidsd.addColumn("45_49mn", "45-49, Male, Negative", hil.getPatientsWithHtsIndicator(45, 49, 'M', '-'), "");
// 		cidsd.addColumn("gt50fn", "50+, Female, Negative", hil.getPatientsWithHtsIndicator(50, 9999, 'F', '-'), "");
// 		cidsd.addColumn("gt50mn", "50+, Male, Negative", hil.getPatientsWithHtsIndicator(50, 9999, 'M', '-'), "");

// 		reportDefinition.addDataSetDefinition("HTS", cidsd, Collections.EMPTY_MAP);
// 		return reportDefinition;
// 	}

// 	@Override
// 	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
// 		String reportDesignUuid = "4712289f-cb07-4d08-a507-141311dfd606";

// 		byte[] contents = new byte[0];
// 		try {
// 			File template = ResourceUtils.getFile("classpath:" + ADX_REPORT_RESOURCE_NAME);
// 			contents = ReportUtil.readByteArrayFromFile(template);
// 		}
// 		catch (IOException e) {
// 			log.error(e);
// 		}

// 		ReportDesign reportDesign = createAdxTemplateDesign(reportDesignUuid, reportDefinition, contents);

// 		return Arrays.asList(reportDesign);
// 	}

// 	private ReportDesign createAdxTemplateDesign(String reportDesignUuid, ReportDefinition reportDefinition, byte[] contents) {
// 		ReportDesign design = new ReportDesign();
// 		design.setUuid(reportDesignUuid);
// 		design.setName("ADX");
// 		design.setReportDefinition(reportDefinition);
// 		design.setRendererType(AdxTemplateRenderer.class);
// 		ReportDesignResource resource = new ReportDesignResource();
// 		resource.setName("template");
// 		resource.setExtension("xml");
// 		resource.setContentType("application/xml");
// 		resource.setContents(contents);
// 		resource.setReportDesign(design);
// 		design.addResource(resource);
// 		return design;
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
