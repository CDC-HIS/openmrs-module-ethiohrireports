package org.openmrs.module.ohrireports.reports.datasetevaluator.datim.tx_curr;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.ALIVE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.PATIENT_STATUS;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.RESTART;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TREATMENT_END_DATE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.tx_curr.TxCurrCoarseByAgeAndSexDataSetDefinition;
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

@Handler(supports = { TxCurrCoarseByAgeAndSexDataSetDefinition.class })
public class TxCurrCoarseByAgeAndSexDataSetDefinitionEvaluator implements DataSetEvaluator {

    private EvaluationContext context;

    private TxCurrCoarseByAgeAndSexDataSetDefinition hdsd;
    private int total = 0;
    @Autowired
    private ConceptService conceptService;

    @Autowired
    private EvaluationService evaluationService;
    List<Obs> obses = new ArrayList<>();

    @Override
    public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext)
            throws EvaluationException {

        hdsd = (TxCurrCoarseByAgeAndSexDataSetDefinition) dataSetDefinition;
        context = evalContext;
        SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);
        total=0;
        DataSetRow femaleDateSet = new DataSetRow();
        obses = LoadObs("F");
        femaleDateSet.addColumnValue(new DataSetColumn("FineByAgeAndSexData", "Gender",
                Integer.class), "Female");
        femaleDateSet.addColumnValue(new DataSetColumn("unkownAge", "Unkown Age", Integer.class),
        getUnknownAgeByGender());

        femaleDateSet.addColumnValue(new DataSetColumn("<15", "<15", Integer.class),
                getEnrolledByAgeAndGender(0, 14));
        femaleDateSet.addColumnValue(new DataSetColumn("15+", "15+", Integer.class),
                getEnrolledByAgeAndGender(15, 200));

        set.addRow(femaleDateSet);

        obses = LoadObs("M");

        DataSetRow maleDataSet = new DataSetRow();
        maleDataSet.addColumnValue(new DataSetColumn("FineByAgeAndSexData", "Gender",
                Integer.class), "Male");
        maleDataSet.addColumnValue(new DataSetColumn("unkownAge", "Unkown Age", Integer.class),
        getUnknownAgeByGender());

        maleDataSet.addColumnValue(new DataSetColumn("<15", "<15", Integer.class),
                getEnrolledByAgeAndGender(0, 14));
        maleDataSet.addColumnValue(new DataSetColumn("15+", "15+", Integer.class),
                getEnrolledByAgeAndGender(15, 200));

        set.addRow(maleDataSet);
        DataSetRow tSetRow = new DataSetRow();
        tSetRow.addColumnValue(new DataSetColumn("subtotal", "Sub-Total", Integer.class),
                total);
        set.addRow(tSetRow);
        return set;
    }
    private int getUnknownAgeByGender(){
        int count=0;
        for (Obs obs : obses) {   
                if ( Objects.isNull(obs.getPerson().getAge())|| obs.getPerson().getAge()==0) {
                count++;
            }
        }
        total = total + count;
        return count;
    }

    private int getEnrolledByAgeAndGender(int min, int max) {
        int count = 0;
        List<Integer> persoIntegers = new ArrayList<>();
        for (Obs obs : obses) {
            if (persoIntegers.contains(obs.getPersonId()))
                continue;
            if (obs.getPerson().getAge() >= min && obs.getPerson().getAge() <= max) {
                count++;
            }

            persoIntegers.add(obs.getPersonId());
        }
        total = total + count;
        // clearCountedPerson(persoIntegers);
        return count;
    }

//     private void clearCountedPerson(List<Integer> personIds) {
//         for (int pId : personIds) {
//             obses.removeIf(p -> p.getPersonId().equals(pId));
//         }
//     }

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
