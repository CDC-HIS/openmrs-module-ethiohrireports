package org.openmrs.module.ohrireports.reports.datasetdefinition.datim;

import java.util.Date;

import org.openmrs.EncounterType;
import org.openmrs.module.reporting.dataset.definition.BaseDataSetDefinition;
import org.openmrs.module.reporting.definition.configuration.ConfigurationProperty;
import org.springframework.stereotype.Component;

@Component
public class TBARTAutoCalculateDataSetDefinition extends BaseDataSetDefinition {
	
	@ConfigurationProperty
	private Date startDate;
	
	@ConfigurationProperty
	private Date endDate;
	
	@ConfigurationProperty
	private EncounterType encounterType;
	
	@ConfigurationProperty
	private boolean isNewlyEnrolled;
	
	public void setNewlyEnrolled(boolean isNewlyEnrolled) {
		this.isNewlyEnrolled = isNewlyEnrolled;
	}
	
	public boolean getIsNewlyEnrolled() {
		return false;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public EncounterType getEncounterType() {
		return encounterType;
	}
	
	public void setEncounterType(EncounterType encounterType) {
		this.encounterType = encounterType;
	}
	
}
