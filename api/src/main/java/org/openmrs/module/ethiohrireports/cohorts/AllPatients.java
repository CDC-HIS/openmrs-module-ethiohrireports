package org.openmrs.module.ethiohrireports.cohorts;

import org.openmrs.module.ethiohrireports.cohorts.manager.BaseCohortDefinitionManager;
import org.openmrs.module.reporting.cohort.definition.AllPatientsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.MessageUtil;
import org.springframework.stereotype.Component;

import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.ALL_PATIENTS_COHORT_UUID;

@Component
public class AllPatients extends BaseCohortDefinitionManager {
	
	@Override
	public String getUuid() {
		return ALL_PATIENTS_COHORT_UUID;
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("ohrireports.all.patients.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("ohrireports.all.patients.reportDescription");
	}
	
	@Override
	public String getVersion() {
		return "0.1";
	}
	
	@Override
	public CohortDefinition newInstance() {
		return new AllPatientsCohortDefinition();
	}
}
