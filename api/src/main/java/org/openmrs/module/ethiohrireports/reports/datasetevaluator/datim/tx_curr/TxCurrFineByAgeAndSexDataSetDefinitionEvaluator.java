package org.openmrs.module.ethiohrireports.reports.datasetevaluator.datim.tx_curr;

import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.ALIVE;
import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.PATIENT_STATUS;
import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.RESTART;
import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.TREATMENT_END_DATE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ethiohrireports.reports.datasetdefinition.datim.tx_curr.TxCurrFineByAgeAndSexDataSetDefinition;
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

@Handler(supports = { TxCurrFineByAgeAndSexDataSetDefinition.class })
public class TxCurrFineByAgeAndSexDataSetDefinitionEvaluator implements DataSetEvaluator {

    private EvaluationContext context;

    private TxCurrFineByAgeAndSexDataSetDefinition hdsd;
    private String title = "Number of adults and children Currently enrolling on antiretroviral therapy (ART)";
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

        hdsd = (TxCurrFineByAgeAndSexDataSetDefinition) dataSetDefinition;
        context = evalContext;
        SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);

        DataSetRow femaleDateSet = new DataSetRow();
        obses = LoadObs("F");
        femaleDateSet.addColumnValue(new DataSetColumn("FineByAgeAndSexData", "Gender",
                Integer.class), "Female");
        femaleDateSet.addColumnValue(new DataSetColumn("unkownAge", "Unkown Age", Integer.class),
        getUnknownAgeByGender());

        femaleDateSet.addColumnValue(new DataSetColumn("<1", "Below One (<1)", Integer.class),
        getEnrolledByAgeAndGender(0, 1));

        buildDataSet(femaleDateSet, "F");



        set.addRow(femaleDateSet);
        obses = LoadObs("M");
        DataSetRow maleDataSet = new DataSetRow();
        maleDataSet.addColumnValue(new DataSetColumn("FineByAgeAndSexData", "Gender",
                Integer.class), "Male");
        maleDataSet.addColumnValue(new DataSetColumn("unkownAge", "Unkown Age", Integer.class),
        getUnknownAgeByGender());
        maleDataSet.addColumnValue(new DataSetColumn("<1", "Below One (<1)", Integer.class),
                getEnrolledByAgeAndGender(0, 1));

        buildDataSet(maleDataSet, "M");

        set.addRow(maleDataSet);
        return set;
    }

    private void buildDataSet(DataSetRow dataSet, String gender) {
        minCount = 1;
        maxCount = 4;
        while (minCount <= 65) {
            if (minCount == 65) {
                dataSet.addColumnValue(new DataSetColumn("65+", "65+", Integer.class),
                        getEnrolledByAgeAndGender(65, 200));
            } else {
                dataSet.addColumnValue(
                        new DataSetColumn(minCount + "-" + maxCount, minCount + "-" + maxCount, Integer.class),
                        getEnrolledByAgeAndGender(minCount, maxCount));
            }

            minCount=1+maxCount;
            maxCount = minCount + 4;
        }
    }
    private int getUnknownAgeByGender(){
        int count=0;
        for (Obs obs : obses) {   
            if ( Objects.isNull(obs.getPerson().getAge())|| obs.getPerson().getAge()==0) {
                count++;
            }
        }

        return count;
    }

    private int getEnrolledByAgeAndGender(int min, int max) {
        int count =0;
    //    List<Integer> persoIntegers = new ArrayList<>();
        for (Obs obs : obses) {
            // if (persoIntegers.contains(obs.getPersonId()))
            //     continue;
            
            if (obs.getPerson().getAge() >= min && obs.getPerson().getAge() <= max) {
                count++;
            }

            // persoIntegers.add(obs.getPersonId());
        }

        // clearCountedPerson(persoIntegers);
        return count;
    }

    private void clearCountedPerson(List<Integer> personIds) {
        for (int pId : personIds) {
            obses.removeIf(p->p.getPersonId().equals(pId));
        }
    }
    private List<Obs> LoadObs(String gender) {

		List<Integer> patientsId = getListOfALiveORRestartPatientObservertions(gender);
		List<Integer> patients = new ArrayList<>();
		List<Obs> localObs = new ArrayList<>();
		if (patientsId == null || patientsId.size() == 0)
			return localObs;
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obs");
		queryBuilder.from(Obs.class, "obs")
				.whereEqual("obs.encounter.encounterType", hdsd.getEncounterType())
				.and()
                .whereEqual("obs.person.gender", gender)
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
						localObs.add(obs);
					  }
				}
		
		return localObs;
	}
    private List<Integer> getListOfALiveORRestartPatientObservertions(String gender) {

		List<Integer> uniqiObs = new ArrayList<>();
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();

		queryBuilder.select("obs")
				.from(Obs.class, "obs")
				.whereEqual("obs.encounter.encounterType", hdsd.getEncounterType())
				.and()
                .whereEqual("obs.person.gender", gender)
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
