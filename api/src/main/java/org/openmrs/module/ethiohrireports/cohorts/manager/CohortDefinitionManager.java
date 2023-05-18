package org.openmrs.module.ethiohrireports.cohorts.manager;

import org.openmrs.module.reporting.cohort.definition.CohortDefinition;

/**
 * This is the base interface that should be implemented by any cohort definition author who wishes
 * their CohortDefinition to be automatically installed upon startup.
 */
public interface CohortDefinitionManager {
	
	/**
	 * @return the cohortDefinition
	 */
	public CohortDefinition constructCohortDefinition();
	
}
