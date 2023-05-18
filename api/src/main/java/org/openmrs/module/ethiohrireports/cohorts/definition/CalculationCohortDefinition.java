package org.openmrs.module.ethiohrireports.cohorts.definition;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.openmrs.module.reporting.calculation.PatientDataCalculation;
import org.openmrs.module.reporting.cohort.definition.BaseCohortDefinition;
import org.openmrs.module.reporting.definition.configuration.ConfigurationProperty;

public class CalculationCohortDefinition extends BaseCohortDefinition {
	
	@ConfigurationProperty(required = true, group = "calculation")
	private PatientDataCalculation calculation;
	
	@ConfigurationProperty(group = "calculation")
	private Date onDate;
	
	@ConfigurationProperty(group = "calculation")
	private Object withResult;
	
	@ConfigurationProperty(group = "calculation")
	private Map<String, Object> calculationParameters;
	
	/**
	 * Default constructor
	 */
	public CalculationCohortDefinition() {
	}
	
	/**
	 * Constructs a new calculation based cohort definition
	 * 
	 * @param calculation the calculation
	 */
	public CalculationCohortDefinition(PatientDataCalculation calculation) {
		setCalculation(calculation);
	}
	
	/**
	 * Constructor to populate name and calculation
	 * 
	 * @param name the name
	 * @param calculation the calculation
	 */
	public CalculationCohortDefinition(String name, PatientDataCalculation calculation) {
		setName(name);
		setCalculation(calculation);
	}
	
	/**
	 * @return the calculation
	 */
	public PatientDataCalculation getCalculation() {
		return calculation;
	}
	
	/**
	 * @param calculation the calculation to set
	 */
	public void setCalculation(PatientDataCalculation calculation) {
		this.calculation = calculation;
	}
	
	/**
	 * Gets the date for which to calculate
	 * 
	 * @return the date
	 */
	public Date getOnDate() {
		return onDate;
	}
	
	/**
	 * Sets the date for which to calculate
	 * 
	 * @param onDate the date
	 */
	public void setOnDate(Date onDate) {
		this.onDate = onDate;
	}
	
	/**
	 * Gets the result value required for inclusion in the cohort
	 * 
	 * @return the result value
	 */
	public Object getWithResult() {
		return withResult;
	}
	
	/**
	 * Sets the result value required for inclusion in the cohort
	 * 
	 * @param withResult the result value
	 */
	public void setWithResult(Object withResult) {
		this.withResult = withResult;
	}
	
	/**
	 * Gets the calculation parameters
	 * 
	 * @return the calculation parameters
	 */
	public Map<String, Object> getCalculationParameters() {
		return calculationParameters;
	}
	
	/**
	 * Sets the calculation parameters
	 * 
	 * @param calculationParameters the calculation parameters
	 */
	public void setCalculationParameters(Map<String, Object> calculationParameters) {
		this.calculationParameters = calculationParameters;
	}
	
	/**
	 * Adds a calculation parameter
	 * 
	 * @param name the name
	 * @param value the value
	 */
	public void addCalculationParameter(String name, Object value) {
		if (calculationParameters == null) {
			calculationParameters = new HashMap<String, Object>();
		}
		
		calculationParameters.put(name, value);
	}
}
