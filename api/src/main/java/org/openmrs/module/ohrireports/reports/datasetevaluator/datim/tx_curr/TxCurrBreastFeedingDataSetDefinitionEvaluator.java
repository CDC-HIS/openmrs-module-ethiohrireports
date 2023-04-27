package org.openmrs.module.ohrireports.reports.datasetevaluator.datim.tx_curr;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.CURRENTLY_BREAST_FEEDING_CHILD;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.YES;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.ALIVE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.PATIENT_STATUS;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.RESTART;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TREATMENT_END_DATE;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.tx_curr.TxCurrBreastFeedingStatusDataSetDefinition;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.dataset.SimpleDataSet;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.evaluator.DataSetEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.HqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

@Handler(supports = { TxCurrBreastFeedingStatusDataSetDefinition.class })
public class TxCurrBreastFeedingDataSetDefinitionEvaluator implements DataSetEvaluator {
	
	private EvaluationContext context;
	
	private TxCurrBreastFeedingStatusDataSetDefinition hdsd;
	
	private Concept breastFeeding, breastFeedingYes;
	
	private String title = "Number of adults and children currently enrolling on antiretroviral therapy (ART)";
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private EvaluationService evaluationService;
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		
		hdsd = (TxCurrBreastFeedingStatusDataSetDefinition) dataSetDefinition;
		context = evalContext;
		breastFeeding = conceptService.getConceptByUuid(CURRENTLY_BREAST_FEEDING_CHILD);
		breastFeedingYes = conceptService.getConceptByUuid(YES);
		DataSetRow dataSet = new DataSetRow();
		dataSet.addColumnValue(new DataSetColumn("breastFeeding", "BreastFeeding", Integer.class),
		    getNumberOfEnrolledBreastFeeding());
		SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);
		set.addRow(dataSet);
		return set;
	}
	
	public int getNumberOfEnrolledBreastFeeding() {
		List<Integer> pList = getTotalEnrolledFemalePatients();
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("distinct obs.personId").from(Obs.class, "obs").whereEqual("obs.concept", breastFeeding).and()
		        .whereEqual("obs.valueCoded", breastFeedingYes).and().whereIn("obs.personId", pList);
		List<Integer> personIDs = evaluationService.evaluateToList(queryBuilder, Integer.class, context);
		return personIDs.size();
		
	}
	
	private List<Integer> getTotalEnrolledFemalePatients() {

		List<Integer> patientsId = getListOfALiveORRestartPatientObservertions();
		List<Integer> patients = new ArrayList<>();
        if (patientsId == null || patientsId.size() == 0)
                return patients;
        HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
        queryBuilder.select("obs");
        queryBuilder.from(Obs.class, "obs")
        .whereEqual("obs.encounter.encounterType", hdsd.getEncounterType())
        .and()
        .whereEqual("obs.concept", conceptService.getConceptByUuid(TREATMENT_END_DATE))
        .and()
        .whereGreater("obs.valueDatetime", hdsd.getEndDate())
        .and()
        .whereLess("obs.obsDatetime", hdsd.getEndDate())
        .whereIdIn("obs.personId", patientsId)
        .orderDesc("obs.personId,obs.obsDatetime");
        for (Obs obs : evaluationService.evaluateToList(queryBuilder, Obs.class, context)) {
                if(!patients.contains(obs.getPersonId()))
                        {
                        patients.add(obs.getPersonId());
                        }
        }
		return patients;
	}
	
	private List<Integer> getListOfALiveORRestartPatientObservertions() {

		List<Integer> uniqiObs = new ArrayList<>();
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();

		queryBuilder.select("obs")
				.from(Obs.class, "obs")
				.whereEqual("obs.encounter.encounterType", hdsd.getEncounterType())
                .and()
				.whereEqual("obs.concept", conceptService.getConceptByUuid(PATIENT_STATUS))
				.and()
				.whereIn("obs.valueCoded", Arrays.asList(conceptService.getConceptByUuid(ALIVE),
						conceptService.getConceptByUuid(RESTART)))
				.and().whereLess("obs.obsDatetime", hdsd.getEndDate());
		queryBuilder.orderDesc("obs.personId,obs.obsDatetime");

		List<Obs> aliveObs = evaluationService.evaluateToList(queryBuilder, Obs.class, context);

		for (Obs obs : aliveObs) {
			if (!uniqiObs.contains(obs.getPersonId())) {
				uniqiObs.add(obs.getPersonId());
			}
		}

		return uniqiObs;
	}
}
