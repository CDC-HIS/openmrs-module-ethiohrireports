package org.openmrs.module.ohrireports.reports.datim;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.HTS_FOLLOW_UP_ENCOUNTER_TYPE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.*;

import org.openmrs.EncounterType;
import org.openmrs.api.context.Context;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.pmtct_art.PMTCTARTAutoCalculateDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.tb_art.TBARTAutoCalculateDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.tb_art.PMTCTARTDataSetDefinition;
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
public class PMTCTARTReport implements ReportManager {
	
	private EncounterType followUpEncounter;
	
	@Override
	public String getUuid() {
		return "59182764-c3a4-41bf-84ad-855d0804f89b";
	}
	
	@Override
	public String getName() {
		return DATIM_REPORT + "-PMTCT_ART";
	}
	
	@Override
	public String getDescription() {
		return "Number of HIV-positive pregnant women who received ART to reduce the risk of mother-to-child-transmission";
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
		
		PMTCTARTAutoCalculateDataSetDefinition tbADataSet = new PMTCTARTAutoCalculateDataSetDefinition();
		tbADataSet.addParameters(getParameters());
		tbADataSet.setEncounterType(followUpEncounter);
		reportDefinition
		        .addDataSetDefinition(
		            "During pregnancy.Numerator will auto-calculate from the Maternal Regimen Type Desegregates",
		            map(tbADataSet, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		PMTCTARTDataSetDefinition alreadyOnARTSetDefinition = new PMTCTARTDataSetDefinition();
		alreadyOnARTSetDefinition.addParameters(getParameters());
		alreadyOnARTSetDefinition.setEncounterType(followUpEncounter);
		reportDefinition.addDataSetDefinition("Disaggregated by Regiment Type:",
		    map(alreadyOnARTSetDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		PMTCTARTDataSetDefinition newlyEnrolledSetDefinition = new PMTCTARTDataSetDefinition();
		newlyEnrolledSetDefinition.addParameters(getParameters());
		newlyEnrolledSetDefinition.setEncounterType(followUpEncounter);
		//newlyEnrolledSetDefinition.setNewlyEnrolled(true);
		reportDefinition.addDataSetDefinition("Disaggregated by Regiment Type:",
		    map(newlyEnrolledSetDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		return reportDefinition;
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		ReportDesign design = ReportManagerUtil.createExcelDesign("c3ef9586-cadd-4ce3-bc07-3e1ed79c994d", reportDefinition);
		
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
