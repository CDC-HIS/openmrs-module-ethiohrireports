package org.openmrs.module.ethiohrireports.cohorts.care_and_treatment;

import org.openmrs.api.context.Context;
import org.openmrs.module.ethiohrireports.cohorts.manager.BaseCohortDefinitionManager;
import org.openmrs.module.reporting.cohort.definition.BaseObsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.DateObsCohortDefinition;
import org.openmrs.module.reporting.common.MessageUtil;
import org.openmrs.module.reporting.common.RangeComparator;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.*;

@Component
public class TodayzAppointments extends BaseCohortDefinitionManager {
	
	@Override
	public String getUuid() {
		return TODAYZ_APPOINTMENTS;
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("ohrireports.clients.care.today.appointments.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("ohrireports.clients.care.today.appointments.reportDescription");
	}
	
	@Override
	public CohortDefinition constructCohortDefinition() {
		DateObsCohortDefinition cd = (DateObsCohortDefinition) super.constructCohortDefinition();
		// TODO: should we narrow down the scope to clients with C&T encounters?
		//	cd.addEncounterType(Context.getEncounterService().getEncounterTypeByUuid(
		//		    CARE_AND_TREATMENT_SERVICE_ENROLLMENT_ENCOUNTER_TYPE));
		cd.setQuestion(Context.getConceptService().getConceptByUuid(RETURN_VISIT_DATE));
		cd.setTimeModifier(BaseObsCohortDefinition.TimeModifier.LAST);
		cd.setOperator1(RangeComparator.EQUAL);
		cd.addParameter(new Parameter("value1", "appointmentDate", Date.class, null, null, null));
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
