// package org.openmrs.module.ohrireports.reports.query;

// import java.util.ArrayList;
// import java.util.Date;
// import java.util.List;

// import org.openmrs.Concept;
// import org.openmrs.Encounter;
// import org.openmrs.EncounterType;
// import org.openmrs.Location;
// import org.openmrs.module.reporting.definition.configuration.ConfigurationProperty;
// import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
// import org.openmrs.module.reporting.evaluation.caching.Caching;
// import org.openmrs.module.reporting.query.BaseQuery;
// import org.openmrs.module.reporting.query.encounter.definition.EncounterQuery;

// @Caching(strategy = ConfigurationPropertyCachingStrategy.class)
// public class ObsEncounterQuery extends BaseQuery<Encounter> implements EncounterQuery {

// 	@ConfigurationProperty
// 	private Concept question;

// 	@ConfigurationProperty
// 	private Date encounterOnOrAfter;

// 	@ConfigurationProperty
// 	private Date encounterOnOrBefore;

// 	@ConfigurationProperty
// 	private Date obsValueOnOrAfter;

// 	public Date getObsValueOnOrAfter() {
// 		return obsValueOnOrAfter;
// 	}

// 	public void setObsValueOnOrAfter(Date obsValueOnOrAfter) {
// 		this.obsValueOnOrAfter = obsValueOnOrAfter;
// 	}

// 	@ConfigurationProperty
// 	private Date obsValueOnOrBefore;

// 	public Date getObsValueOnOrBefore() {
// 		return obsValueOnOrBefore;
// 	}

// 	public void setObsValueOnOrBefore(Date obsValueOnOrBefore) {
// 		this.obsValueOnOrBefore = obsValueOnOrBefore;
// 	}

// 	@ConfigurationProperty
// 	private List<Location> encounterLocations;

// 	@ConfigurationProperty
// 	private List<EncounterType> encounterTypes;

// 	//***** PROPERTY ACCESS *****

// 	public Concept getQuestion() {
// 		return question;
// 	}

// 	public void setQuestion(Concept question) {
// 		this.question = question;
// 	}

// 	public Date getEncounterOnOrAfter() {
// 		return encounterOnOrAfter;
// 	}

// 	public void setEncounterOnOrAfter(Date encounterOnOrAfter) {
// 		this.encounterOnOrAfter = encounterOnOrAfter;
// 	}

// 	public Date getEncounterOnOrBefore() {
// 		return encounterOnOrBefore;
// 	}

// 	public void setEncounterOnOrBefore(Date encounterOnOrBefore) {
// 		this.encounterOnOrBefore = encounterOnOrBefore;
// 	}

// 	public List<Location> getEncounterLocations() {
// 		return encounterLocations;
// 	}

// 	public void setEncounterLocations(List<Location> encounterLocations) {
// 		this.encounterLocations = encounterLocations;
// 	}

// 	public void addEncounterLocation(Location location) {
// 		if (encounterLocations == null) {
// 			encounterLocations = new ArrayList<Location>();
// 		}
// 		encounterLocations.add(location);
// 	}

// 	public List<EncounterType> getEncounterTypes() {
// 		return encounterTypes;
// 	}

// 	public void setEncounterTypes(List<EncounterType> encounterTypes) {
// 		this.encounterTypes = encounterTypes;
// 	}

// 	public void addEncounterType(EncounterType encounterType) {
// 		if (encounterTypes == null) {
// 			encounterTypes = new ArrayList<EncounterType>();
// 		}
// 		encounterTypes.add(encounterType);
// 	}
// }
