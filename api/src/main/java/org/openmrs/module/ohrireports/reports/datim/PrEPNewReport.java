package org.openmrs.module.ohrireports.reports.datim;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.openmrs.EncounterType;
import org.openmrs.api.context.Context;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.pr_ep_new.AutoCalculatePrepNewDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.pr_ep_new.DisaggregatedByPopulationTypDatasetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.pr_ep_new.PrEPNewDatasetDefinition;
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
import static org.openmrs.module.ohrireports.OHRIReportsConstants.DATIM_REPORT;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.REPORT_VERSION;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.HTS_FOLLOW_UP_ENCOUNTER_TYPE;

@Component
public class PrEPNewReport implements ReportManager {
	
	@Override
	public String getUuid() {
		return "24fbeafe-f8c7-4f28-a3bd-d31ed89d21b7";
	}
	
	@Override
	public String getName() {
		return DATIM_REPORT + "-PrEP_New";
	}
	
	@Override
	public String getDescription() {
		return "Number of individuals who where newly enrolled on pre-exposure prophylaxis (PrEP) to prevent HIV infection in the reporting period";
	}
	
	@Override
	public List<Parameter> getParameters() {
		Parameter startDate = new Parameter("startDate", "Start Date", Date.class);
		startDate.setRequired(true);
		Parameter startDateGC = new Parameter("startDateGC", " ", Date.class);
		startDateGC.setRequired(false);
		Parameter endDate = new Parameter("endDate", "End Date", Date.class);
		endDate.setRequired(true);
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
		reportDefinition.addParameters(getParameters());
		
		EncounterType followUpEncounter = Context.getEncounterService().getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE);
		
		AutoCalculatePrepNewDataSetDefinition aDataSetDefinition = new AutoCalculatePrepNewDataSetDefinition();
		aDataSetDefinition.setParameters(getParameters());
		aDataSetDefinition.setEncounterType(followUpEncounter);
		reportDefinition.addDataSetDefinition("Auto-Calculate",
		    map(aDataSetDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		PrEPNewDatasetDefinition dataSetDefinition = new PrEPNewDatasetDefinition();
		dataSetDefinition.setParameters(getParameters());
		dataSetDefinition.setEncounterType(Context.getEncounterService()
		        .getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE));
		reportDefinition.addDataSetDefinition("Disaggregated by Age / Sex",
		    map(dataSetDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		DisaggregatedByPopulationTypDatasetDefinition dDataSetDefinition = new DisaggregatedByPopulationTypDatasetDefinition();
		dDataSetDefinition.setParameters(getParameters());
		dDataSetDefinition.setEncounterType(followUpEncounter);
		reportDefinition.addDataSetDefinition("Disaggregated by key population type",
		    map(dDataSetDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		return reportDefinition;
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		ReportDesign design = ReportManagerUtil.createExcelDesign("ba0df56a-2902-4c7f-a32f-e7f552431105", reportDefinition);
		
		return Arrays.asList(design);
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
	public List<ReportRequest> constructScheduledRequests(ReportDefinition reportDefinition) {
		return null;
	}
	
	@Override
	public String getVersion() {
		return REPORT_VERSION;
	}
	
}
