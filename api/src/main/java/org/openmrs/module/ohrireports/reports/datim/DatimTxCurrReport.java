package org.openmrs.module.ohrireports.reports.datim;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.HTS_FOLLOW_UP_ENCOUNTER_TYPE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.DATIM_REPORT;

import org.openmrs.api.context.Context;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.TxCurrAutoCalculateDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.TxCurrCoarseByAgeAndSexDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.TxCurrFineByAgeAndSexDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.TxCurrKeyPopulationTypeDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.TxCurrARVDataSetDefinition;
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
public class DatimTxCurrReport implements ReportManager {
	
	@Override
	public String getUuid() {
		return "7529482a-e57c-47d3-9dc3-57c4ad9e28bf";
	}
	
	@Override
	public String getName() {
		return DATIM_REPORT + "-Tx-Curr";
	}
	
	@Override
	public String getDescription() {
		return "Aggregate report of DATIM TX_CURR enrolling  patients";
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
		
		TxCurrAutoCalculateDataSetDefinition aDefinition = new TxCurrAutoCalculateDataSetDefinition();
		aDefinition.addParameters(getParameters());
		aDefinition.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE));
		aDefinition.setDescription("Number of adults and children currently enrolling on antiretroviral therapy (ART)");
		reportDefinition.addDataSetDefinition("Auto-Calculate",
		    map(aDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		TxCurrFineByAgeAndSexDataSetDefinition fDefinition = new TxCurrFineByAgeAndSexDataSetDefinition();
		fDefinition.addParameters(getParameters());
		fDefinition.setDescription("Disaggregated by Age/Sex (Fine disaggregate)");
		fDefinition.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE));
		reportDefinition.addDataSetDefinition("Required Disaggregated by Age/Sex (Fine disaggregate)",
		    map(fDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		TxCurrCoarseByAgeAndSexDataSetDefinition cDefinition = new TxCurrCoarseByAgeAndSexDataSetDefinition();
		cDefinition.addParameters(getParameters());
		cDefinition.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE));
		cDefinition.setDescription("Disaggregated by Age/Sex (Coarse disaggregated)");
		reportDefinition.addDataSetDefinition("Conditional Disaggregated by Age/Sex (Coarse disaggregated)",
		    map(cDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		// TxCurrBreastFeedingStatusDataSetDefinition bDefinition = new TxCurrBreastFeedingStatusDataSetDefinition();
		// bDefinition.addParameters(getParameters());
		// bDefinition.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE));
		// bDefinition.setDescription("Disaggregated by Breastfeeding Status at ART Initiation");
		
		// reportDefinition.addDataSetDefinition("Breast-Feeding-Status",
		//     map(bDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		TxCurrARVDataSetDefinition arvDefinition = new TxCurrARVDataSetDefinition();
		arvDefinition.addParameters(getParameters());
		arvDefinition.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid(HTS_FOLLOW_UP_ENCOUNTER_TYPE));
		arvDefinition.setDescription("Disaggregated by ARV Dispensing Quantity by Coarse Age/Sex ");
		
		reportDefinition.addDataSetDefinition("Required Disaggregated by ARV Dispensing Quantity by Coarse Age/Sex",
		    map(arvDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		TxCurrKeyPopulationTypeDataSetDefinition keyPopulationTypeDefinition = new TxCurrKeyPopulationTypeDataSetDefinition();
		keyPopulationTypeDefinition.addParameters(getParameters());
		keyPopulationTypeDefinition.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid(
		    HTS_FOLLOW_UP_ENCOUNTER_TYPE));
		keyPopulationTypeDefinition.setDescription("Disaggregated by key population type");
		
		reportDefinition.addDataSetDefinition("Required Disaggregated by key population type",
		    map(keyPopulationTypeDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
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
		ReportDesign design = ReportManagerUtil.createExcelDesign("bfe17f94-383a-472b-8eac-17fc5bef95a1`", reportDefinition);
		
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
