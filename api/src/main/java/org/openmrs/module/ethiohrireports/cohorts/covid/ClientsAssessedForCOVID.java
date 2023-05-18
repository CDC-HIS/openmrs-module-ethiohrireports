package org.openmrs.module.ethiohrireports.cohorts.covid;

import org.openmrs.api.context.Context;
import org.openmrs.module.ethiohrireports.cohorts.manager.BaseCohortDefinitionManager;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.EncounterCohortDefinition;
import org.openmrs.module.reporting.common.MessageUtil;
import org.springframework.stereotype.Component;

import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.CLIENTS_ASSESSED_FOR_COVID;
import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.COVID_ASSESSMENT_ENCOUNTER_TYPE;

@Component
public class ClientsAssessedForCOVID extends BaseCohortDefinitionManager {
	
	@Override
	public String getUuid() {
		return CLIENTS_ASSESSED_FOR_COVID;
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("ohrireports.clients.accessed.covid.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("ohrireports.clients.accessed.covid.reportDescription");
	}
	
	@Override
	public CohortDefinition constructCohortDefinition() {
		EncounterCohortDefinition cd = (EncounterCohortDefinition) super.constructCohortDefinition();
		cd.addEncounterType(Context.getEncounterService().getEncounterTypeByUuid(COVID_ASSESSMENT_ENCOUNTER_TYPE));
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
