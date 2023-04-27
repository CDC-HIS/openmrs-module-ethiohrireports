package org.openmrs.module.ohrireports.reports.datim;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.HTS_FOLLOW_UP_ENCOUNTER_TYPE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.DATIM_REPORT;

import org.openmrs.api.context.Context;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.tx_cx_ca.CxCaTxAutoCalculateDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.tx_cx_ca.CxCaTxByAgeandTreatmentTypeandScreeningVisitTypeDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.tx_cx_ca.CxCaTxPostTreatmentFollowupDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.tx_cx_ca.CxCaTxRescreenedDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.evaluation.parameter.Parameterizable;
import org.openmrs.module.reporting.evaluation.parameter.ParameterizableUtil;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.ReportRequest;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.ReportManager;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.springframework.stereotype.Component;

@Component
public class DatimCxCaTxReport implements ReportManager {
	
	@Override
	public String getUuid() {
		return "7529cxca-e57c-47d3-9dc3-57c4ad9e28bf";
	}
	
	@Override
	public String getName() {
		return DATIM_REPORT + "-Cx_CA_Tx";
	}
	
	@Override
	public String getDescription() {
		return "Calculate the total number of HIV positive women who are currently on ART and received treatment for cervical cancer in the reporting period";
	}
	
	@Override
	public List<Parameter> getParameters() {
		Parameter startDate = new Parameter("startDate", "Start Date", Date.class);
		startDate.setRequired(false);
		Parameter startDateGC = new Parameter("startDateGC", " ", Date.class);
		startDateGC.setRequired(false);
		Parameter endDate = new Parameter("endDate", "End Date", Date.class);
		endDate.setRequired(false);
		Parameter endDateGC = new Parameter("endDateGC", " ", Date.class);
		endDateGC.setRequired(false);
		return Arrays.asList(startDate, startDateGC, endDate, endDateGC);
	}
	
	@Override
	public ReportDefinition constructReportDefinition() {
		ReportDefinition reportDefinition = new ReportDefinition();
		reportDefinition.setUuid(getUuid());
		reportDefinition.setName(getName());
		reportDefinition.setDescription(getDescription());
		reportDefinition.setParameters(getParameters());
		
		CxCaTxAutoCalculateDataSetDefinition aDefinition = new CxCaTxAutoCalculateDataSetDefinition();
		aDefinition.addParameters(getParameters());
		aDefinition.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE));
		aDefinition
		        .setDescription("Total Number of female clients Currently on ART and received treatment forCervical Cancer during the reporting period");
		reportDefinition.addDataSetDefinition("Auto-Calculate",
		    map(aDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		CxCaTxByAgeandTreatmentTypeandScreeningVisitTypeDataSetDefinition fDefinition = new CxCaTxByAgeandTreatmentTypeandScreeningVisitTypeDataSetDefinition();
		fDefinition.addParameters(getParameters());
		fDefinition.setDescription("Disaggregated by Age/Treatment Type/Screening Visit Type");
		fDefinition.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE));
		reportDefinition.addDataSetDefinition(
		    "Required Disaggregated by Age/Treatment Type/Screening Visit Type, First time screened for cervical cancer",
		    map(fDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		CxCaTxRescreenedDataSetDefinition rDefinition = new CxCaTxRescreenedDataSetDefinition();
		rDefinition.addParameters(getParameters());
		rDefinition.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE));
		rDefinition.setDescription("Rescreened after previous negative or suspected cancer");
		reportDefinition.addDataSetDefinition("Conditional Rescreened after previous negative or suspected cancer",
		    map(rDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		CxCaTxPostTreatmentFollowupDataSetDefinition pDefinition = new CxCaTxPostTreatmentFollowupDataSetDefinition();
		pDefinition.addParameters(getParameters());
		pDefinition.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE));
		pDefinition.setDescription("Post treatment follow-up");
		reportDefinition.addDataSetDefinition("Conditional Post treatment follow-up",
		    map(pDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
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
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		ReportDesign design = ReportManagerUtil.createExcelDesign("bfe1cxca-383a-472b-8eac-17fc5bef95a1`", reportDefinition);
		
		return Arrays.asList(design);
		
	}
	
	@Override
	public List<ReportRequest> constructScheduledRequests(ReportDefinition reportDefinition) {
		return null;
	}
	
	@Override
	public String getVersion() {
		return "1.0.0-SNAPSHOT";
		
	}
	
}
