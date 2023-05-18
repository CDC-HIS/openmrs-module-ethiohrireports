/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ethiohrireports.reports.hts;

import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.HTS_FOLLOW_UP_ENCOUNTER_TYPE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.ethiohrireports.reports.datasetdefinition.TXCurrDataSetDefinitionAdx;
import org.openmrs.module.ethiohrireports.reports.library.EncounterDataLibrary;
import org.openmrs.module.ethiohrireports.reports.library.PatientDataLibrary;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.evaluation.parameter.Parameterizable;
import org.openmrs.module.reporting.evaluation.parameter.ParameterizableUtil;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.ReportRequest;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.ReportManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TX_CUR_ReportAdx implements ReportManager {
	
	@Autowired
	EncounterService encounterService;
	
	@Autowired
	ConceptService conceptService;
	
	@Autowired
	PatientDataLibrary hpdl;
	
	@Autowired
	EncounterDataLibrary hedl;
	
	@Override
	public String getUuid() {
		return "4e79374e-b8a0-455e-a571-a9a9a801f113";
	}
	
	@Override
	public String getName() {
		return "TX CURR ADX";
	}
	
	@Override
	public String getDescription() {
		return null;
	}
	
	@Override
	public List<Parameter> getParameters() {
		
		Parameter endDate = new Parameter("endDate", "On Month", Date.class);
		endDate.setRequired(true);
		Parameter endDateGC = new Parameter("endDateGC", " ", Date.class);
		endDateGC.setRequired(true);
		return Arrays.asList(endDate, endDateGC);
		
	}
	
	@Override
	public ReportDefinition constructReportDefinition() {
		ReportDefinition reportDefinition = new ReportDefinition();
		reportDefinition.setUuid(getUuid());
		reportDefinition.setName(getName());
		reportDefinition.setDescription(getDescription());
		
		reportDefinition.setParameters(getParameters());
		TXCurrDataSetDefinitionAdx txCurrDataSetDefinition = new TXCurrDataSetDefinitionAdx();
		txCurrDataSetDefinition.addParameters(getParameters());
		txCurrDataSetDefinition.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid(
		    HTS_FOLLOW_UP_ENCOUNTER_TYPE));
		reportDefinition.addDataSetDefinition("Tx-Curr Aggregate by Age and Gender",
		    map(txCurrDataSetDefinition, "endDate=${endDateGC}"));
		reportDefinition.addDataSetDefinition("Tx-Curr-one", map(txCurrDataSetDefinition, "endDate=${endDateGC}"));
		reportDefinition.addDataSetDefinition("Tx-Curr-two", map(txCurrDataSetDefinition, "endDate=${endDateGC}"));
		return reportDefinition;
	}
	
	public static <T extends Parameterizable> Mapped<T> map(T parameterizable, String mappings) {
		if (parameterizable == null) {
			throw new IllegalArgumentException("Parameterizable cannot be null");
		}
		if (mappings == null) {
			mappings = ""; // probably not necessary, just to be safe
		}
		return new Mapped<T>(parameterizable, ParameterizableUtil.createParameterMappings(mappings));
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition){
		
		return new ArrayList<>();
	}
	
	@Override
	public List<ReportRequest> constructScheduledRequests(ReportDefinition reportDefinition) {
		return new ArrayList<>();
	}
	
	@Override
	public String getVersion() {
		return "1.0.0-SNAPSHOT";
	}
	
}
