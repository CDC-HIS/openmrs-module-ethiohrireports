package org.openmrs.module.ethiohrireports.cohorts.hts;

import org.openmrs.api.context.Context;
import org.openmrs.module.ethiohrireports.cohorts.manager.BaseCohortDefinitionManager;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.EncounterCohortDefinition;
import org.openmrs.module.reporting.common.MessageUtil;
import org.springframework.stereotype.Component;

import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.HTS_CLIENTS;
import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.HTS_ENCOUNTER_TYPE;

@Component
public class HTSClients extends BaseCohortDefinitionManager {
	
	@Override
	public String getUuid() {
		return HTS_CLIENTS;
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("ohrireports.clients.hts.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("ohrireports.clients.hts.reportDescription");
	}
	
	@Override
	public CohortDefinition constructCohortDefinition() {
		EncounterCohortDefinition cd = (EncounterCohortDefinition) super.constructCohortDefinition();
		cd.addEncounterType(Context.getEncounterService().getEncounterTypeByUuid(HTS_ENCOUNTER_TYPE));
		return cd;
	}
	
	@Override
	public String getVersion() {
		return "0.1";
	}
	
	@Override
	public CohortDefinition newInstance() {
		return new EncounterCohortDefinition();
	}
}
