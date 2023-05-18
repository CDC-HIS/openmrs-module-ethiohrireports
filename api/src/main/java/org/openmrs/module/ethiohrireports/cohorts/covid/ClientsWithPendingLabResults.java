package org.openmrs.module.ethiohrireports.cohorts.covid;

import org.openmrs.module.ethiohrireports.cohorts.manager.BaseCohortDefinitionManager;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.common.MessageUtil;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameterizable;
import org.openmrs.module.reporting.evaluation.parameter.ParameterizableUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.*;

@Component
public class ClientsWithPendingLabResults extends BaseCohortDefinitionManager {
	
	@Override
	public String getUuid() {
		return COVID_CLIENTS_WITH_PENDING_LAB_RESULTS;
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("ohrireports.clients.covid.with.pending.lab.results.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("ohrireports.clients.covid.with.pending.lab.results.reportDescription");
	}
	
	@Override
	public String getVersion() {
		return "0.1";
	}
	
	@Override
	public CohortDefinition newInstance() {
		return new CompositionCohortDefinition();
	}
	
	@Override
	public CohortDefinition constructCohortDefinition() {
		CompositionCohortDefinition cd = (CompositionCohortDefinition) super.constructCohortDefinition();
		Parameterizable clientsWithCollectedSamples = getReferencedCohorts().get(COVID_CLIENTS_WITH_COLLECTED_SAMPLES) != null ? getReferencedCohorts()
		        .get(COVID_CLIENTS_WITH_COLLECTED_SAMPLES) : ParameterizableUtil.getParameterizable(
		    COVID_CLIENTS_WITH_COLLECTED_SAMPLES, CohortDefinition.class);
		Parameterizable clientsWithConfirmedLabResults = getReferencedCohorts()
		        .get(COVID_CLIENTS_WITH_CONFIRMED_LAB_RESULTS) != null ? getReferencedCohorts().get(
		    COVID_CLIENTS_WITH_CONFIRMED_LAB_RESULTS) : ParameterizableUtil.getParameterizable(
		    COVID_CLIENTS_WITH_CONFIRMED_LAB_RESULTS, CohortDefinition.class);
		Mapped mapping1 = new Mapped();
		mapping1.setParameterizable(clientsWithCollectedSamples);
		cd.addSearch("Clients-with-collected-samples", mapping1);
		Mapped mapping2 = new Mapped();
		mapping2.setParameterizable(clientsWithConfirmedLabResults);
		cd.addSearch("Clients-with-confirmed-Results", mapping2);
		cd.setCompositionString("Clients-with-collected-samples AND NOT Clients-with-confirmed-Results");
		return cd;
	}
	
	@Override
	public Map<String, Parameterizable> getReferencedCohorts() {
		Map<String, Parameterizable> referencedCohorts = super.getReferencedCohorts();
		if (referencedCohorts == null) {
			referencedCohorts = new HashMap<String, Parameterizable>() {
				
				{
					put(COVID_CLIENTS_WITH_COLLECTED_SAMPLES, null);
					put(COVID_CLIENTS_WITH_CONFIRMED_LAB_RESULTS, null);
				}
			};
		}
		return referencedCohorts;
	}
}
