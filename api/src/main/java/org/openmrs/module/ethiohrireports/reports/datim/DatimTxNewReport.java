package org.openmrs.module.ethiohrireports.reports.datim;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.HTS_FOLLOW_UP_ENCOUNTER_TYPE;
import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.DATIM_REPORT;

import org.openmrs.EncounterType;
import org.openmrs.api.context.Context;
import org.openmrs.module.ethiohrireports.reports.datasetdefinition.datim.tx_new.AutoCalculateDataSetDefinition;
import org.openmrs.module.ethiohrireports.reports.datasetdefinition.datim.tx_new.BreastFeedingStatusDataSetDefinition;
import org.openmrs.module.ethiohrireports.reports.datasetdefinition.datim.tx_new.CoarseByAgeAndSexDataSetDefinition;
import org.openmrs.module.ethiohrireports.reports.datasetdefinition.datim.tx_new.FineByAgeAndSexDataSetDefinition;
import org.openmrs.module.ethiohrireports.reports.datasetdefinition.datim.tx_new.PopulationTypeDataSetDefinition;
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
public class DatimTxNewReport implements ReportManager {
	
	private EncounterType followUpEncounter;
	
	@Override
	public String getUuid() {
		return "9f9f13aa-65cb-44c2-a2e8-1ff058f8c959";
	}
	
	@Override
	public String getName() {
		return DATIM_REPORT + "-Tx_New";
	}
	
	@Override
	public String getDescription() {
		return "Aggregate report of DATIM TXnew lists a newly enrolled  patients";
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
		
		AutoCalculateDataSetDefinition aDefinition = new AutoCalculateDataSetDefinition();
		aDefinition.addParameters(getParameters());
		aDefinition.setEncounterType(followUpEncounter);
		aDefinition.setDescription("Number of adults and children newly enrolled on antiretroviral therapy (ART)");
		reportDefinition.addDataSetDefinition("Auto-Calculate",
		    map(aDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		FineByAgeAndSexDataSetDefinition fDefinition = new FineByAgeAndSexDataSetDefinition();
		fDefinition.addParameters(getParameters());
		fDefinition.setDescription("Disaggregated by Age/Sex (Fine disaggregate)");
		fDefinition.setEncounterType(followUpEncounter);
		reportDefinition.addDataSetDefinition("Required", map(fDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		CoarseByAgeAndSexDataSetDefinition cDefinition = new CoarseByAgeAndSexDataSetDefinition();
		cDefinition.addParameters(getParameters());
		cDefinition.setEncounterType(followUpEncounter);
		cDefinition.setDescription("Disaggregated by Age/Sex (Coarse disaggregated)");
		reportDefinition.addDataSetDefinition("Conditional",
		    map(cDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		BreastFeedingStatusDataSetDefinition bDefinition = new BreastFeedingStatusDataSetDefinition();
		bDefinition.addParameters(getParameters());
		bDefinition.setEncounterType(followUpEncounter);
		bDefinition.setDescription("Disaggregated by Breastfeeding Status at ART Initiation");
		reportDefinition.addDataSetDefinition("Breast-Feeding-ART-Status",
		    map(bDefinition, "startDate=${startDateGC},endDate=${endDateGC}"));
		
		PopulationTypeDataSetDefinition pDefinition = new PopulationTypeDataSetDefinition();
		pDefinition.addParameters(getParameters());
		pDefinition.setEncounterType(followUpEncounter);
		pDefinition.setDescription("Disaggregated by Key population-type");
		reportDefinition.addDataSetDefinition("Breast-Feeding-Status",
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
		ReportDesign design = ReportManagerUtil.createExcelDesign("c29ab966-7727-4e66-95e9-d1aeba22caf1", reportDefinition);
		
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
