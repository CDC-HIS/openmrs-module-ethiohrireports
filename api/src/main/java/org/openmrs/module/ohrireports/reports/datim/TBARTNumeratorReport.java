package org.openmrs.module.ohrireports.reports.datim;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.HTS_FOLLOW_UP_ENCOUNTER_TYPE;


import org.openmrs.EncounterType;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.Report;
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
        return "TB_ART (Numerator)";
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
            //Auto-calculate Numerator
            //Disaggregated by Age/sex/result
            //
        return reportDefinition;
    }

    @Override
    public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
        ReportDesign design = ReportManagerUtil.createExcelDesign("43cc0259-0c07-44c6-a4f5-0201fcb2d55d",
                reportDefinition);

        return Arrays.asList(design);

    }

    @Override
    public List<ReportRequest> constructScheduledRequests(ReportDefinition reportDefinition) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'constructScheduledRequests'");
    }

    @Override
    public String getVersion() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVersion'");
    }

}
