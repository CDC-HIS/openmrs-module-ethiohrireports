package org.openmrs.module.ohrireports.reports.datasetevaluator.datim;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.ART_START_DATE;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.AutoCalculateDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.FineByAgeAndSexDataSetDefinition;
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

@Handler(supports = { FineByAgeAndSexDataSetDefinition.class })
public class FineByAgeAndSexDataSetDefinitionEvaluator implements DataSetEvaluator {

    private EvaluationContext context;

    private FineByAgeAndSexDataSetDefinition hdsd;
    private Concept artConcept;
    private String title = "Number of adults and children newly enrolled on antiretroviral therapy (ART)";
    @Autowired
    private ConceptService conceptService;

    @Autowired
    private EvaluationService evaluationService;
    private int minCount = 0;
    private int maxCount = 4;
    List<Obs> obses = new ArrayList<>();

    @Override
    public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext)
            throws EvaluationException {

        hdsd = (FineByAgeAndSexDataSetDefinition) dataSetDefinition;
        context = evalContext;
        artConcept = conceptService.getConceptByUuid(ART_START_DATE);
        SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);

        DataSetRow femaleDateSet = new DataSetRow();
        obses = LoadObs("F");
        femaleDateSet.addColumnValue(new DataSetColumn("FineByAgeAndSexData", "Gender",
                Integer.class), "Female");
        femaleDateSet.addColumnValue(new DataSetColumn("unkownAge", "Unkown Age", Integer.class),
                getEnrolledByAgeAndGender(0, 0, "F"));

        femaleDateSet.addColumnValue(new DataSetColumn("<1", "Below One (<1)", Integer.class),
                getEnrolledByAgeAndGender(0, 1, "F"));

        buildDataSet(femaleDateSet, "F");



        set.addRow(femaleDateSet);
        obses = LoadObs("M");
        DataSetRow maleDataSet = new DataSetRow();
        maleDataSet.addColumnValue(new DataSetColumn("FineByAgeAndSexData", "Gender",
                Integer.class), "Male");
        maleDataSet.addColumnValue(new DataSetColumn("unkownAge", "Unkown Age", Integer.class),
                getEnrolledByAgeAndGender(0, 0, "M"));

        maleDataSet.addColumnValue(new DataSetColumn("<1", "Below One (<1)", Integer.class),
                getEnrolledByAgeAndGender(0, 1, "M"));

        buildDataSet(maleDataSet, "M");

        set.addRow(maleDataSet);
        return set;
    }

    private void buildDataSet(DataSetRow dataSet, String gender) {
        minCount = 0;
        maxCount = 4;
        while (maxCount >= 49) {
            if (maxCount == 49) {
                dataSet.addColumnValue(new DataSetColumn("50+", "50+", Integer.class),
                        getEnrolledByAgeAndGender(50, 50, gender));
            } else {
                dataSet.addColumnValue(
                        new DataSetColumn(minCount + "-" + maxCount, minCount + "-" + maxCount, Integer.class),
                        getEnrolledByAgeAndGender(minCount, maxCount, gender));
            }

            minCount=minCount+maxCount;
            maxCount = minCount + 5;
        }
    }

    private int getEnrolledByAgeAndGender(int min, int max, String gender) {
        int count =0;
       List<Integer> persoIntegers = new ArrayList<>();
        for (Obs obs : obses) {
            if (persoIntegers.contains(obs.getPersonId()))
                continue;
           if (min >= 50 && max >= 50 && obs.getPerson().getAge() <=50) {
                count++;
            } else if (obs.getPerson().getAge() >= min && obs.getPerson().getAge() <= max) {
                count++;
            }

            persoIntegers.add(obs.getPersonId());
        }

        clearCountedPerson(persoIntegers);
        return count;
    }

    private void clearCountedPerson(List<Integer> personIds) {
        for (int pId : personIds) {
            obses.removeIf(p->p.getPersonId().equals(pId));
        }
    }

    private List<Obs> LoadObs(String gender) {
        HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
        queryBuilder.select("obs")
                .from(Obs.class, "obs")
                .whereEqual("obs.encounter.encounterType", hdsd.getEncounterType())
                .and()
                .whereEqual("obs.person.gender", gender)
                .and()
                .whereEqual("obs.concept", artConcept)
                .and()
                .whereGreaterOrEqualTo("obs.valueDatetime", hdsd.getStartDate())
                .and()
                .whereLessOrEqualTo("obs.valueDatetime", hdsd.getEndDate())
                .orderDesc("obs.obsDatetime");

        List<Obs> obses = evaluationService.evaluateToList(queryBuilder, Obs.class, context);
        return obses;
    }

}
