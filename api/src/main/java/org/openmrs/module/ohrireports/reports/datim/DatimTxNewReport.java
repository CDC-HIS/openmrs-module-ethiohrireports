package org.openmrs.module.ohrireports.reports.datim;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.HTS_FOLLOW_UP_ENCOUNTER_TYPE;

import org.openmrs.EncounterType;
import org.openmrs.api.context.Context;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.AutoCalculateDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.BreastFeedingStatusDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.CoarseByAgeAndSexDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.FineByAgeAndSexDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.PopulationTypeDataSetDefinition;
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
		return "7529482a-e57c-47d3-9dc3-57c4ad9e28bf";
	}
	
	@Override
	public String getName() {
		return "DATIM-Tx-New";
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
		ReportDesign design = ReportManagerUtil.createExcelDesign("43cc0259-0c07-44c6-a4f5-0201fcb2d55d", reportDefinition);
		
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
