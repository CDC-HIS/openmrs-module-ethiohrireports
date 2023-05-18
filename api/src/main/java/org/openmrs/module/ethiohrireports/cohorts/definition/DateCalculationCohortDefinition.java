package org.openmrs.module.ethiohrireports.cohorts.definition;

import java.util.Date;

import org.openmrs.module.reporting.calculation.PatientDataCalculation;
import org.openmrs.module.reporting.definition.configuration.ConfigurationProperty;

public class DateCalculationCohortDefinition extends CalculationCohortDefinition {
	
	@ConfigurationProperty(required = false, group = "date range")
	private Date onOrAfter;
	
	@ConfigurationProperty(required = false, group = "date range")
	private Date onOrBefore;
	
	public DateCalculationCohortDefinition() {
	}
	
	/**
	 * Constructs a new calculation based cohort definition
	 * 
	 * @param calculation the calculation
	 */
	public DateCalculationCohortDefinition(PatientDataCalculation calculation) {
		super(calculation);
	}
	
	/**
	 * @return the onOrAfter
	 */
	public Date getOnOrAfter() {
		return onOrAfter;
	}
	
	/**
	 * @param onOrAfter the onOrAfter to set
	 */
	public void setOnOrAfter(Date onOrAfter) {
		this.onOrAfter = onOrAfter;
	}
	
	/**
	 * @return the onOrBefore
	 */
	public Date getOnOrBefore() {
		return onOrBefore;
	}
	
	/**
	 * @param onOrBefore the onOrBefore to set
	 */
	public void setOnOrBefore(Date onOrBefore) {
		this.onOrBefore = onOrBefore;
	}
}
