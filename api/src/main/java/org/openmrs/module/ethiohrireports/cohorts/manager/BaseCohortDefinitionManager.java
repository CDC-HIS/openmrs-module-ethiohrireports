package org.openmrs.module.ethiohrireports.cohorts.manager;

import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameterizable;

import java.util.Map;

public abstract class BaseCohortDefinitionManager implements CohortDefinitionManager {
	
	private Map<String, Parameterizable> referencedCohorts;
	
	/**
	 * @return cohortDefinition uuid
	 */
	public abstract String getUuid();
	
	/**
	 * @return cohortDefinition name
	 */
	public abstract String getName();
	
	/**
	 * @return cohortDefinition description
	 */
	public abstract String getDescription();
	
	/**
	 * @return version
	 */
	public abstract String getVersion();
	
	/**
	 * Instantiates a new {@link CohortDefinition} ie.
	 * 
	 * <pre>
	 *  return new DateObsCohortDefinition();
	 * </pre>
	 */
	public abstract CohortDefinition newInstance();
	
	/**
	 * Determines whether the cohort definition should be loaded at startup
	 */
	public Boolean isActivated() {
		return true;
	}
	
	public Map<String, Parameterizable> getReferencedCohorts() {
		return referencedCohorts;
	}
	
	public void setReferencedCohorts(Map<String, Parameterizable> referencedCohorts) {
		this.referencedCohorts = referencedCohorts;
	}
	
	@Override
	public CohortDefinition constructCohortDefinition() {
		CohortDefinition cd = newInstance();
		cd.setUuid(getUuid());
		cd.setName(getName());
		cd.setDescription(getDescription());
		return cd;
	}
}
