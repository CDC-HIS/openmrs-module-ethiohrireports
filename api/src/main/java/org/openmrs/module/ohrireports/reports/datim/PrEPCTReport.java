package org.openmrs.module.ohrireports.reports.datim;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.openmrs.EncounterType;
import org.openmrs.api.context.Context;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.pr_ep_ct.AutoCalculatePrEPCTDatasetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.pr_ep_ct.PrEPCTByPopulationTypeDatasetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.pr_ep_ct.PrEPCTDatasetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.pr_ep_ct.PrEPCTPregnantBreastfeedingDatasetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.pr_ep_ct.PrEPCTTestResultDatasetDefinition;
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
public class PrEPCTReport implements ReportManager {
	
	@Override
	public String getUuid() {
		return "17ec12f4-7475-4deb-9b4a-ee8df5c667a3";
	}
	
	@Override
	public String getName() {
		return DATIM_REPORT + "-PrEP_CT";
	}
	
	@Override
	public String getDescription() {
		return "Number of individuals, excluding those newly enrolled, that return for a follow-up visit or reinitiation vist to receive pre-exposure prophylaxis (PrEP) to prevent HIV during the reporting period. Numerator will auto-Calculate from sum of Age/Sex Disaggregate";
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
		
		AutoCalculatePrEPCTDatasetDefinition aDataSetDefinition = new AutoCalculatePrEPCTDatasetDefinition();
		aDataSetDefinition.setParameters(getParameters());
		aDataSetDefinition.setEncounterType(followUpEncounter);
		reportDefinition.addDataSetDefinition("Auto-Calculate",
		    map(aDataSetDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		PrEPCTDatasetDefinition dataSetDefinition = new PrEPCTDatasetDefinition();
		dataSetDefinition.setParameters(getParameters());
		dataSetDefinition.setEncounterType(followUpEncounter);
		reportDefinition.addDataSetDefinition("Disaggregated by Age / Sex",
		    map(dataSetDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		PrEPCTTestResultDatasetDefinition prEPCTTestDataset = new PrEPCTTestResultDatasetDefinition();
		prEPCTTestDataset.addParameters(getParameters());
		prEPCTTestDataset.setEncounterType(followUpEncounter);
		reportDefinition.addDataSetDefinition("Disaggregated by test result",
		    map(prEPCTTestDataset, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		PrEPCTByPopulationTypeDatasetDefinition prEPCTDataset = new PrEPCTByPopulationTypeDatasetDefinition();
		prEPCTDataset.addParameters(getParameters());
		prEPCTDataset.setEncounterType(followUpEncounter);
		reportDefinition.addDataSetDefinition("Disaggregated by key population type",
		    map(prEPCTDataset, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		PrEPCTPregnantBreastfeedingDatasetDefinition prEPCPFDataset = new PrEPCTPregnantBreastfeedingDatasetDefinition();
		prEPCPFDataset.addParameters(getParameters());
		prEPCPFDataset.setEncounterType(followUpEncounter);
		reportDefinition.addDataSetDefinition("Disaggregated by Pregnant/Breastfeeding",
		    map(prEPCPFDataset, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		return reportDefinition;
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		ReportDesign design = ReportManagerUtil.createExcelDesign("fa0b3ea1-cf63-4dd0-9a69-d9d9af804590", reportDefinition);
		
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
