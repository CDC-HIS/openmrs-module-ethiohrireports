package org.openmrs.module.ohrireports.reports.datim;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.HTS_FOLLOW_UP_ENCOUNTER_TYPE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.*;

import org.openmrs.api.context.Context;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.TxTbDenominatorAutoCalculateDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.TxTbDenominatorARTByAgeAndSexDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.TxTbDenominatorSpecimenSentDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.TxTbDenominatorPositiveResultReturnedDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.TxTbDenominatorDiagnosticTestDataSetDefinition;
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
public class DatimTxTbDenominatorReport implements ReportManager {
	
	@Override
	public String getUuid() {
		return "3172dd1a-ca9b-4146-9053-b48b3428dd21";
	}
	
	@Override
	public String getName() {
		return DATIM_REPORT + "-Tx_Tb_Denominator";
	}
	
	@Override
	public String getDescription() {
		return "Aggregate report of DATIM TX_TB_Denominator patients";
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
		
		TxTbDenominatorAutoCalculateDataSetDefinition aDefinition = new TxTbDenominatorAutoCalculateDataSetDefinition();
		aDefinition.addParameters(getParameters());
		aDefinition.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE));
		aDefinition
		        .setDescription("Number of ART patients who were screened for TB at least once during the reporting period. Denominator will auto-calculate from Start on ART by Screen Result by Age/Sex");
		reportDefinition
		        .addDataSetDefinition(
		            "Auto-Calculate : Number of ART patients who were screened for TB at least once during the reporting period. Denominator will auto-calculate from Start on ART by Screen Result by Age/Sex",
		            map(aDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		TxTbDenominatorARTByAgeAndSexDataSetDefinition cDefinition = new TxTbDenominatorARTByAgeAndSexDataSetDefinition();
		cDefinition.addParameters(getParameters());
		cDefinition.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE));
		cDefinition.setDescription("Disaggregated by Start of ART Screen Result by Age/Sex");
		reportDefinition.addDataSetDefinition("Required : Disaggregated by Start of ART Screen Result by Age/Sex",
		    map(cDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		TxTbDenominatorSpecimenSentDataSetDefinition sDefinition = new TxTbDenominatorSpecimenSentDataSetDefinition();
		sDefinition.addParameters(getParameters());
		sDefinition.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE));
		sDefinition.setDescription("Disaggregated by Specimen Sent");
		reportDefinition.addDataSetDefinition("Required : Disaggregated by Specimen Sent",
		    map(sDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		TxTbDenominatorDiagnosticTestDataSetDefinition tDefinition = new TxTbDenominatorDiagnosticTestDataSetDefinition();
		tDefinition.addParameters(getParameters());
		tDefinition.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE));
		tDefinition.setDescription("Disaggregated by Specimen Sent and Diagnostic Test");
		reportDefinition.addDataSetDefinition("Required : [Disagg by Specimen Sent] Diagnostic Test",
		    map(tDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		TxTbDenominatorPositiveResultReturnedDataSetDefinition pDefinition = new TxTbDenominatorPositiveResultReturnedDataSetDefinition();
		pDefinition.addParameters(getParameters());
		pDefinition.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE));
		pDefinition.setDescription("Disaggregated by Positive Result Returned");
		reportDefinition.addDataSetDefinition("Required: Disaggregated by Positive Result Returned",
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
		ReportDesign design = ReportManagerUtil.createExcelDesign("006145f4-a8bb-4876-ad6d-f2a020778534", reportDefinition);
		
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
