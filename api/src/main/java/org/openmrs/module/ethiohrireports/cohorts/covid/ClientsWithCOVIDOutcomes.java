package org.openmrs.module.ethiohrireports.cohorts.covid;

import org.openmrs.api.context.Context;
import org.openmrs.module.ethiohrireports.cohorts.manager.BaseCohortDefinitionManager;
import org.openmrs.module.reporting.cohort.definition.BaseObsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CodedObsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.MessageUtil;
import org.springframework.stereotype.Component;

import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.*;

@Component
public class ClientsWithCOVIDOutcomes extends BaseCohortDefinitionManager {
	
	@Override
	public String getUuid() {
		return CLIENTS_WITH_COVID_OUTCOMES;
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("ohrireports.clients.covid.outcomes.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("ohrireports.clients.covid.outcomes.reportDescription");
	}
	
	@Override
	public CohortDefinition constructCohortDefinition() {
		CodedObsCohortDefinition cd = (CodedObsCohortDefinition) super.constructCohortDefinition();
		cd.addEncounterType(Context.getEncounterService().getEncounterTypeByUuid(COVID_ASSESSMENT_ENCOUNTER_TYPE));
		cd.setQuestion(Context.getConceptService().getConceptByUuid(COVID_TREATMENT_OUTCOME));
		cd.setTimeModifier(BaseObsCohortDefinition.TimeModifier.LAST);
		return cd;
	}
	
	@Override
	public String getVersion() {
		return "0.1";
	}
	
	@Override
	public CohortDefinition newInstance() {
		return new CodedObsCohortDefinition();
	}
}
