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

import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.indicator.BaseIndicator;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HtsIndicatorLibrary extends BaseIndicator {
	
	@Autowired
	EncountersForPatientDataLibrary efpdl;
	
	public Mapped<CohortIndicator> getPatientsWithHtsIndicator(Integer minAge, Integer maxAge, char gender, char res) {
		CohortIndicator ci = new CohortIndicator();
		ci.setCohortDefinition(efpdl.getPatientsWithHtsCohort(minAge, maxAge, gender, res));
		ci.setType(CohortIndicator.IndicatorType.COUNT);
		return Mapped.mapStraightThrough(ci);
	}
}
