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

import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.module.reporting.cohort.definition.*;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDataDefinition;
import org.openmrs.module.reporting.data.encounter.definition.ObsForEncounterDataDefinition;
import org.openmrs.module.reporting.definition.library.BaseDefinitionLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.*;

@Component
public class EncounterDataLibrary extends BaseDefinitionLibrary<EncounterDataDefinition> {
	
	@Autowired
	ConceptService conceptService;
	
	@Autowired
	EncounterService encounterService;
	
	@Override
	public Class<? super EncounterDataDefinition> getDefinitionType() {
		return EncounterDataDefinition.class;
	}
	
	@Override
	public String getKeyPrefix() {
		return "ohri.encounterData.hts.";
	}
	
	public ObsForEncounterDataDefinition getObsValue(String conceptUuid) {
		ObsForEncounterDataDefinition ofedd = new ObsForEncounterDataDefinition();
		ofedd.setQuestion(conceptService.getConceptByUuid(conceptUuid));
		ofedd.setSingleObs(true);
		ofedd.setRetired(true);
		return ofedd;
	}
	
	public ObsForEncounterDataDefinition getObsValue(String conceptUuid, String answerUuid) {
		ObsForEncounterDataDefinition ofedd = new ObsForEncounterDataDefinition();
		ofedd.setQuestion(conceptService.getConceptByUuid(conceptUuid));
		Concept answer = conceptService.getConceptByUuid(answerUuid);
		ofedd.setAnswers(Collections.singletonList(answer));
		ofedd.setSingleObs(true);
		return ofedd;
	}
	
	public ObsForEncounterDataDefinition getObsValueIncludes(String conceptUuid, List<String> answersUuid) {
		ObsForEncounterDataDefinition ofedd = new ObsForEncounterDataDefinition();
		ofedd.setQuestion(conceptService.getConceptByUuid(conceptUuid));
		List<Concept> answers = new ArrayList<>();
		for (String answerUUID : answersUuid) {
			answers.add(conceptService.getConceptByUuid(answerUUID));
		}
		//conceptService.getConceptByUuid(answerUuid);
		ofedd.setAnswers(answers);
		ofedd.setSingleObs(true);
		return ofedd;
	}
	
	public ObsForEncounterDataDefinition getObsValues(String conceptUuid) {
		ObsForEncounterDataDefinition ofedd = new ObsForEncounterDataDefinition();
		ofedd.setQuestion(conceptService.getConceptByUuid(conceptUuid));
		ofedd.setSingleObs(false);
		return ofedd;
	}
	
	public EncounterWithCodedObsCohortDefinition getHtsEncountersCohort(char c) {
		EncounterWithCodedObsCohortDefinition ecd = new EncounterWithCodedObsCohortDefinition();
		ecd.addEncounterType(encounterService.getEncounterTypeByUuid(HTS_ENCOUNTER_TYPE));
		ecd.addEncounterType(encounterService.getEncounterTypeByUuid(HTS_RETROSPECTIVE_ENCOUNTER_TYPE));
		ecd.setConcept(conceptService.getConceptByUuid(FINAL_HIV_RESULT));
		if (c == '+') {
			ecd.setIncludeCodedValues(Collections.singletonList(conceptService.getConceptByUuid(POSITIVE)));
		} else if (c == '-') {
			ecd.setIncludeCodedValues(Collections.singletonList(conceptService.getConceptByUuid(NEGATIVE)));
		}
		return ecd;
	}
}
