package org.openmrs.module.ethiohrireports.cohorts.covid;

import org.openmrs.api.context.Context;
import org.openmrs.module.ethiohrireports.cohorts.manager.BaseCohortDefinitionManager;
import org.openmrs.module.reporting.cohort.definition.BaseObsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CodedObsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.MessageUtil;
import org.openmrs.module.reporting.common.SetComparator;
import org.springframework.stereotype.Component;

import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.*;

@Component
public class COVID19PositiveClients extends BaseCohortDefinitionManager {
	
	@Override
	public String getUuid() {
		return "COVID19_POSITIVE_CLIENTS";
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("ohrireports.clients.covid.positive.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("ohrireports.clients.covid.positive.reportDescription");
	}
	
	@Override
	public CohortDefinition constructCohortDefinition() {
		CodedObsCohortDefinition cd = (CodedObsCohortDefinition) super.constructCohortDefinition();
		cd.addEncounterType(Context.getEncounterService().getEncounterTypeByUuid(COVID_ASSESSMENT_ENCOUNTER_TYPE));
		cd.setQuestion(Context.getConceptService().getConceptByUuid(FINAL_COVID19_TEST_RESULT));
		cd.setOperator(SetComparator.IN);
		cd.addValue(Context.getConceptService().getConceptByUuid(POSITIVE));
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
