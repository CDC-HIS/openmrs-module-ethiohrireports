package org.openmrs.module.ethiohrireports.reports.datim;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.HTS_FOLLOW_UP_ENCOUNTER_TYPE;
import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.*;

import org.openmrs.EncounterType;
import org.openmrs.api.context.Context;
import org.openmrs.module.ethiohrireports.reports.datasetdefinition.datim.tb_art.TBARTAutoCalculateDataSetDefinition;
import org.openmrs.module.ethiohrireports.reports.datasetdefinition.datim.tb_art.TBARTDataSetDefinition;
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
public class TBARTNumeratorReport implements ReportManager {
	
	private EncounterType followUpEncounter;
	
	@Override
	public String getUuid() {
		return "af7c1fe6-d669-414e-b066-e9733f0de7a8";
	}
	
	@Override
	public String getName() {
		return DATIM_REPORT + "-TB_ART (Numerator)";
	}
	
	@Override
	public String getDescription() {
		return "Proportion of HIV-positive new and relapsed TB cases on ART during TB treatment";
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
		followUpEncounter = Context.getEncounterService().getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE);
		
		TBARTAutoCalculateDataSetDefinition tbADataSet = new TBARTAutoCalculateDataSetDefinition();
		tbADataSet.addParameters(getParameters());
		tbADataSet.setEncounterType(followUpEncounter);
		reportDefinition
		        .addDataSetDefinition(
		            "Number of TB cases with documented HIV-positive status who start or continue ART during the reporting period. ",
		            map(tbADataSet, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		TBARTDataSetDefinition alreadyOnARTSetDefinition = new TBARTDataSetDefinition();
		alreadyOnARTSetDefinition.addParameters(getParameters());
		alreadyOnARTSetDefinition.setEncounterType(followUpEncounter);
		//alreadyOnARTSetDefinition.setNewlyEnrolled(false);
		reportDefinition.addDataSetDefinition("Disaggregated by Age/Sex/Result Already on ART",
		    map(alreadyOnARTSetDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		TBARTDataSetDefinition newlyEnrolledSetDefinition = new TBARTDataSetDefinition();
		newlyEnrolledSetDefinition.addParameters(getParameters());
		newlyEnrolledSetDefinition.setEncounterType(followUpEncounter);
		//newlyEnrolledSetDefinition.setNewlyEnrolled(true);
		reportDefinition.addDataSetDefinition("Disaggregated by Age/Sex/Result Newly on ARt",
		    map(newlyEnrolledSetDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		return reportDefinition;
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		ReportDesign design = ReportManagerUtil.createExcelDesign("81fa27e6-4685-49e4-9e37-ae11e679f4d5", reportDefinition);
		
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
		return "1.0.0-SNAPSHOT";
	}
	
}
