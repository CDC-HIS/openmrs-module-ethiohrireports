package org.openmrs.module.ethiohrireports.cohorts.covid;

import org.openmrs.module.ethiohrireports.cohorts.manager.BaseCohortDefinitionManager;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.common.MessageUtil;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameterizable;
import org.openmrs.module.reporting.evaluation.parameter.ParameterizableUtil;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.*;

@Component
public class ClientsWithoutCOVID19Outcomes extends BaseCohortDefinitionManager {
	
	@Override
	public String getUuid() {
		return CLIENTS_WITHOUT_COVID_19_OUTCOMES;
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("ohrireports.clients.covid.without.outcomes.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("ohrireports.clients.covid.without.outcomes.reportDescription");
	}
	
	@Override
	public CohortDefinition constructCohortDefinition() {
		CompositionCohortDefinition cd = (CompositionCohortDefinition) super.constructCohortDefinition();
		Parameterizable clientsAssessedForCovid = getReferencedCohorts().get(CLIENTS_ASSESSED_FOR_COVID) != null ? getReferencedCohorts()
		        .get(CLIENTS_ASSESSED_FOR_COVID) : ParameterizableUtil.getParameterizable(CLIENTS_ASSESSED_FOR_COVID,
		    CohortDefinition.class);
		Parameterizable clientsWithCovidOutcomes = getReferencedCohorts().get(CLIENTS_WITH_COVID_OUTCOMES) != null ? getReferencedCohorts()
		        .get(CLIENTS_WITH_COVID_OUTCOMES) : ParameterizableUtil.getParameterizable(CLIENTS_WITH_COVID_OUTCOMES,
		    CohortDefinition.class);
		Mapped mapping1 = new Mapped();
		mapping1.setParameterizable(clientsAssessedForCovid);
		cd.addSearch("Clients-assessed-for-COVID", mapping1);
		Mapped mapping2 = new Mapped();
		mapping2.setParameterizable(clientsWithCovidOutcomes);
		cd.addSearch("Clients-with-COVID-Outcomes", mapping2);
		cd.setCompositionString("Clients-assessed-for-COVID AND NOT Clients-with-COVID-Outcomes");
		return cd;
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
	public Map<String, Parameterizable> getReferencedCohorts() {
		Map<String, Parameterizable> referencedCohorts = super.getReferencedCohorts();
		if (referencedCohorts == null) {
			referencedCohorts = new HashMap<String, Parameterizable>() {
				
				{
					put(CLIENTS_ASSESSED_FOR_COVID, null);
					put(CLIENTS_WITH_COVID_OUTCOMES, null);
				}
			};
		}
		return referencedCohorts;
	}
}
