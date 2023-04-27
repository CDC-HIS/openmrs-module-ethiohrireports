package org.openmrs.module.ohrireports.reports.datasetevaluator.datim.pr_ep_ct;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.TDF_FTC_DRUG;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TDF_TENOFOVIR_DRUG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.TDF_3TC_DRUG;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.PR_EP_STARTED;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.pr_ep_ct.PrEPCTDatasetDefinition;
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

@Handler(supports = { PrEPCTDatasetDefinition.class })
public class PrEPCTDatasetDefinitionEvaluator implements DataSetEvaluator {
	
	@Autowired
	ConceptService conceptService;
	
	@Autowired
	EvaluationService evaluationService;
	
	private PrEPCTDatasetDefinition auCDataSetDefinition;
	
	private EvaluationContext context;
	
	private Concept tdfConcept, tdf_ftcConcept, tdf3tcConcept, prEpStatedConcept;
	
	private int total, minCount, maxCount;
	
	private List<Obs> obses;
	
	@Override
    public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext)
            throws EvaluationException {
        auCDataSetDefinition = (PrEPCTDatasetDefinition) dataSetDefinition;
        obses = new ArrayList<>();
        context = evalContext;
        loadConcepts();
        SimpleDataSet dataSet = new SimpleDataSet(dataSetDefinition, evalContext);
        buildDataSet(dataSet);
        return dataSet;
    }
	
	private void loadConcepts() {
		tdfConcept = conceptService.getConceptByUuid(TDF_TENOFOVIR_DRUG);
		tdf_ftcConcept = conceptService.getConceptByUuid(TDF_FTC_DRUG);
		tdf3tcConcept = conceptService.getConceptByUuid(TDF_3TC_DRUG);
		prEpStatedConcept = conceptService.getConceptByUuid(PR_EP_STARTED);
	}
	
	private void buildDataSet(SimpleDataSet simpleDataSet) {
		getAll("F");
		DataSetRow femaleSetRow = new DataSetRow();
		buildDataSet(femaleSetRow, "F");
		simpleDataSet.addRow(femaleSetRow);
		
		getAll("M");
		DataSetRow maleSetRow = new DataSetRow();
		buildDataSet(maleSetRow, "M");
		simpleDataSet.addRow(maleSetRow);
	}
	
	private void buildDataSet(DataSetRow dataSet, String gender) {
		total = 0;
		minCount = 15;
		maxCount = 19;
		
		dataSet.addColumnValue(new DataSetColumn("ByAgeAndSexData", "Gender", Integer.class), gender.equals("F") ? "Female"
		        : "Male");
		dataSet.addColumnValue(new DataSetColumn("unknownAge", "Unknown Age", Integer.class), getEnrolledByUnknownAge());
		
		while (minCount <= 50) {
			if (minCount == 50) {
				dataSet.addColumnValue(new DataSetColumn("50+", "50+", Integer.class),
				    getEnrolledByAgeAndGender(50, 200, gender));
			} else {
				dataSet.addColumnValue(
				    new DataSetColumn(minCount + "-" + maxCount, minCount + "-" + maxCount, Integer.class),
				    getEnrolledByAgeAndGender(minCount, maxCount, gender));
			}
			minCount = maxCount + 1;
			maxCount = minCount + 4;
		}
		dataSet.addColumnValue(new DataSetColumn("Sub-total", "Subtotal", Integer.class), total);
	}
	
	private int getEnrolledByAgeAndGender(int min, int max, String gender) {
        int count = 0;
        List<Integer> personIds = new ArrayList<>();
        for (Obs obs : obses) {

            if (personIds.contains(obs.getPersonId()))
                continue;

            if (obs.getPerson().getAge() >= min && obs.getPerson().getAge() <= max) {
                personIds.add(obs.getPersonId());
                count++;
            }
        }
        incrementTotalCount(count);
        clearCountedPerson(personIds);
        return count;
    }
	
	private int getEnrolledByUnknownAge() {
        int count = 0;
        List<Integer> personIds = new ArrayList<>();
        for (Obs obs : obses) {
            if (personIds.contains(obs.getPersonId()))
                continue;

            if (Objects.isNull(obs.getPerson().getAge()) ||
                    obs.getPerson().getAge() <= 0) {
                count++;
                personIds.add(obs.getPersonId());
            }

        }
        incrementTotalCount(count);
        clearCountedPerson(personIds);
        return count;
    }
	
	private void getAll(String gender) {
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs").from(Obs.class, "obs")
		        .whereEqual("obs.encounter.encounterType", auCDataSetDefinition.getEncounterType())
		        .whereIn("obs.valueCoded", Arrays.asList(tdfConcept, tdf3tcConcept, tdf_ftcConcept)).and()
		        .whereEqual("obs.person.gender", gender).and()
		        .whereLess("obs.obsDatetime", auCDataSetDefinition.getStartDate()).and()
		        .whereIn("obs.personId", getOnPrEpPatients());
		obses = evaluationService.evaluateToList(queryBuilder, Obs.class, context);
		
	}
	
	private void incrementTotalCount(int count) {
		if (count > 0)
			total = total + count;
	}
	
	private void clearCountedPerson(List<Integer> personIds) {
        for (int pId : personIds) {
            obses.removeIf(p -> p.getPersonId().equals(pId));
        }
    }
	
	private List<Integer> getPreviouslyOnPrEpPatients() {
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs").from(Obs.class, "obs")
		
		.whereEqual("obs.concept", prEpStatedConcept).and()
		        .whereLess("obs.valueDatetime", auCDataSetDefinition.getStartDate());
		return evaluationService.evaluateToList(queryBuilder, Integer.class, context);
	}
	
	private List<Integer> getOnPrEpPatients() {
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder
		        .select("obs")
		        .from(Obs.class, "obs")
		        .whereEqual("obs.encounter.encounterType", auCDataSetDefinition.getEncounterType())
		        .and()
		        .whereEqual("obs.concept", prEpStatedConcept)
		        .and()
		        .whereBetweenInclusive("obs.valueDatetime", auCDataSetDefinition.getStartDate(),
		            auCDataSetDefinition.getEndDate()).and().whereIn("obs.personId", getPreviouslyOnPrEpPatients());
		return evaluationService.evaluateToList(queryBuilder, Integer.class, context);
	}
}
