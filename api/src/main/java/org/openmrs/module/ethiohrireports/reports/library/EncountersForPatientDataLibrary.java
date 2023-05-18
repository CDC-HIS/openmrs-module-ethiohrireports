/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ethiohrireports.reports.library;

import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.data.patient.definition.EncountersForPatientDataDefinition;
import org.openmrs.module.reporting.definition.library.BaseDefinitionLibrary;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EncountersForPatientDataLibrary extends BaseDefinitionLibrary<EncountersForPatientDataDefinition> {
	
	@Autowired
	PatientDataLibrary pdl;
	
	@Autowired
	EncounterDataLibrary edl;
	
	@Override
	public Class<? super EncountersForPatientDataDefinition> getDefinitionType() {
		return EncountersForPatientDataDefinition.class;
	}
	
	@Override
	public String getKeyPrefix() {
		return "ohri.patientEncounterData.hts.";
	}
	
	public Mapped<CohortDefinition> getPatientsWithHtsCohort(Integer minAge, Integer maxAge, char gender, char res) {
		CompositionCohortDefinition ccd = new CompositionCohortDefinition();
		ccd.addSearch("agedBetween", Mapped.mapStraightThrough(pdl.getAgeCohort(minAge, maxAge)));
		ccd.addSearch("isGender", Mapped.mapStraightThrough(pdl.getPatientsByGenderCohort(gender)));
		ccd.addSearch("hasHtsEncounter", Mapped.mapStraightThrough(edl.getHtsEncountersCohort(res)));
		ccd.setCompositionString("agedBetween and isGender and hasHtsEncounter");
		return Mapped.mapStraightThrough(ccd);
	}
}
