package org.openmrs.module.ethiohrireports.cohorts.covid;

import org.openmrs.api.context.Context;
import org.openmrs.module.ethiohrireports.cohorts.manager.BaseCohortDefinitionManager;
import org.openmrs.module.reporting.cohort.definition.BaseObsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.DateObsCohortDefinition;
import org.openmrs.module.reporting.common.MessageUtil;
import org.springframework.stereotype.Component;

import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.*;

@Component
public class ClientsWithCollectedSamples extends BaseCohortDefinitionManager {
	
	@Override
	public String getUuid() {
		return COVID_CLIENTS_WITH_COLLECTED_SAMPLES;
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("ohrireports.clients.covid.collected.samples.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("ohrireports.clients.covid.collected.samples.reportDescription");
	}
	
	@Override
	public CohortDefinition constructCohortDefinition() {
		DateObsCohortDefinition cd = (DateObsCohortDefinition) super.constructCohortDefinition();
		cd.addEncounterType(Context.getEncounterService().getEncounterTypeByUuid(COVID_ASSESSMENT_ENCOUNTER_TYPE));
		cd.setQuestion(Context.getConceptService().getConceptByUuid(SPECIMEN_COLLECTION_DATE));
		cd.setTimeModifier(BaseObsCohortDefinition.TimeModifier.LAST);
		return cd;
	}
	
	@Override
	public String getVersion() {
		return "0.1";
	}
	
	@Override
	public CohortDefinition newInstance() {
		return new DateObsCohortDefinition();
	}
}
