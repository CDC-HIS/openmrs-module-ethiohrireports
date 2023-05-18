package org.openmrs.module.ethiohrireports.reports.datasetevaluator.datim.tx_cx_ca;

import static org.openmrs.module.ethiohrireports.OHRIReportsConstants.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ethiohrireports.reports.datasetdefinition.datim.tx_cx_ca.CxCaTxRescreenedDataSetDefinition;
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

@Handler(supports = { CxCaTxRescreenedDataSetDefinition.class })
public class CxCaTxRescreenedDataSetDefinitionEvaluator implements DataSetEvaluator {

    private EvaluationContext context;

    private CxCaTxRescreenedDataSetDefinition hdsd;
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

        hdsd = (CxCaTxRescreenedDataSetDefinition) dataSetDefinition;
        context = evalContext;
        SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);

        DataSetRow cryotherapy = new DataSetRow();
        obses = getByScreenType(CXCA_TREATMENT_TYPE_CRYOTHERAPY);
        cryotherapy.addColumnValue(new DataSetColumn("screenType", "",
                Integer.class), "Cryotherapy");
        cryotherapy.addColumnValue(new DataSetColumn("unknownAge", "Unknown Age", Integer.class),
        getUnknownAgeByGender());
        buildDataSet(cryotherapy);
        cryotherapy.addColumnValue(new DataSetColumn("subtotal", "Subtotal", Integer.class),
        obses.size());
        set.addRow(cryotherapy);

        obses = getByScreenType(CXCA_TREATMENT_TYPE_LEEP);

        DataSetRow leep = new DataSetRow();
        leep.addColumnValue(new DataSetColumn("screenType", "",
                Integer.class), "Leep");
        leep.addColumnValue(new DataSetColumn("unknownAge", "Unknown Age", Integer.class),
        getUnknownAgeByGender());
        buildDataSet(leep);
        leep.addColumnValue(new DataSetColumn("subtotal", "Subtotal", Integer.class),
                obses.size());
        set.addRow(leep);
        
        obses = getByScreenType(CXCA_TREATMENT_TYPE_THERMOCOAGULATION);
        DataSetRow thermocoagulation = new DataSetRow();
        thermocoagulation.addColumnValue(new DataSetColumn("screenType", "",
                Integer.class), "Thermocoagulation");
        thermocoagulation.addColumnValue(new DataSetColumn("unknownAge", "Unknown Age", Integer.class),
        getUnknownAgeByGender());
        buildDataSet(thermocoagulation);
        thermocoagulation.addColumnValue(new DataSetColumn("subtotal", "Subtotal", Integer.class),
                obses.size());
        set.addRow(thermocoagulation);
        DataSetRow subtotal = new DataSetRow();
        subtotal.addColumnValue(new DataSetColumn("screenType", "",
                Integer.class), "Total");
        subtotal.addColumnValue(new DataSetColumn("subtotal", "subtotal",Integer.class), getCxCaFirstTimeStarted().size());
        set.addRow(subtotal);
        return set;
    }

    private void buildDataSet(DataSetRow dataSet) {
        minCount = 15;
        maxCount = 19;
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

    private List<Obs> getByScreenType(String screenType) {
		List<Integer> patientsId = getCxCaFirstTimeStarted();
		List<Integer> patients = new ArrayList<>();
        List<Obs> localObs = new ArrayList<>();
        if (patientsId == null || patientsId.size() == 0)
                return localObs;
        HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
        queryBuilder.select("obs").from(Obs.class, "obs")
		.whereEqual("obs.concept", conceptService.getConceptByUuid(CXCA_TREATMENT_STARTING_DATE)).and()
        .whereEqual("obs.encounter.encounterType", hdsd.getEncounterType()).and()
        .whereEqual("obs.valueCoded", conceptService.getConceptByUuid(screenType)).and().whereIdIn("obs.personId", patientsId)
        .and().whereLess("obs.obsDatetime", hdsd.getEndDate());
        queryBuilder.orderDesc("obs.personId,obs.obsDatetime");

        for (Obs obs : evaluationService.evaluateToList(queryBuilder, Obs.class, context)) {
			if(!patients.contains(obs.getPersonId()))
					{
					patients.add(obs.getPersonId());
                    localObs.add(obs);
					}
			}		
		return localObs;
		
	}

    private List<Integer> getCxCaFirstTimeStarted() {
        List<Integer> patientsId = getCxCAStartedPatients();
		List<Integer> patients = new ArrayList<>();
        if (patientsId == null || patientsId.size() == 0)
                return patients;
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();

		queryBuilder.select("obs")
        .from(Obs.class, "obs")
        .whereEqual("obs.encounter.encounterType", hdsd.getEncounterType())
        .and()
        .whereEqual("obs.concept", conceptService.getConceptByUuid(CXCA_TYPE_OF_SCREENING))
        .and()
        .whereEqual("obs.valueCoded", conceptService.getConceptByUuid(CXCA_TYPE_OF_SCREENING_RESCREENING)).and().whereIdIn("obs.personId", patientsId)
        .and().whereLess("obs.obsDatetime", hdsd.getEndDate());
		queryBuilder.orderDesc("obs.personId,obs.obsDatetime");

		List<Obs> liveObs = evaluationService.evaluateToList(queryBuilder, Obs.class, context);

		for (Obs obs : liveObs) {
			if (!patients.contains(obs.getPersonId())) {
				patients.add(obs.getPersonId());
			}
		}

		return patients;
	}
 
    
    private List<Integer> getCxCAStartedPatients() {
		List<Integer> patientsId = getArtstartedPatients();
		List<Integer> patients = new ArrayList<>();
        if (patientsId == null || patientsId.size() == 0)
                return patients;
        HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
        queryBuilder.select("obs").from(Obs.class, "obs")
		.whereEqual("obs.concept", conceptService.getConceptByUuid(CXCA_TREATMENT_STARTING_DATE)).and()
        .whereEqual("obs.encounter.encounterType", hdsd.getEncounterType()).and()
		.whereGreaterOrEqualTo("obs.valueDatetime", hdsd.getStartDate()).and()
        .whereLessOrEqualTo("obs.valueDatetime", hdsd.getEndDate()).and()
        .whereIdIn("obs.personId", patientsId)
        .orderDesc("obs.obsDatetime");

        for (Obs obs : evaluationService.evaluateToList(queryBuilder, Obs.class, context)) {
			if(!patients.contains(obs.getPersonId()))
					{
					patients.add(obs.getPersonId());
					}
			}		
		return patients;
		
	}

	private List<Integer> getArtstartedPatients() {
		List<Integer> patientsId = getDatimTxCurrTotalEnrolledPatients();
		List<Integer> patients = new ArrayList<>();
        if (patientsId == null || patientsId.size() == 0)
                return patients;
        HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
        queryBuilder.select("obs").from(Obs.class, "obs")
		.whereEqual("obs.concept", conceptService.getConceptByUuid(ART_START_DATE)).and()
        .whereEqual("obs.encounter.encounterType", hdsd.getEncounterType()).and()
        .whereLessOrEqualTo("obs.valueDatetime", hdsd.getEndDate()).and()
        .whereIdIn("obs.personId", patientsId)
        .orderDesc("obs.obsDatetime");

        for (Obs obs : evaluationService.evaluateToList(queryBuilder, Obs.class, context)) {
			if(!patients.contains(obs.getPersonId()))
					{
					patients.add(obs.getPersonId());
					}
			}		
		return patients;
		
	}
	
	private List<Integer> getDatimTxCurrTotalEnrolledPatients() {

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
		// patients = evaluationService.evaluateToList(queryBuilder, Integer.class, context);
				
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
                .whereEqual("obs.person.gender", "F")
				.and()
				.whereIn("obs.valueCoded", Arrays.asList(conceptService.getConceptByUuid(ALIVE),conceptService.getConceptByUuid(RESTART)))
				.and().whereLess("obs.obsDatetime", hdsd.getEndDate());
		queryBuilder.orderDesc("obs.personId,obs.obsDatetime");

		List<Obs> liveObs = evaluationService.evaluateToList(queryBuilder, Obs.class, context);

		for (Obs obs : liveObs) {
			if (!uniqiObs.contains(obs.getPersonId())) {
				uniqiObs.add(obs.getPersonId());
				// patientStatus.put(obs.getPersonId(), obs.getValueCoded());
			}
		}

		return uniqiObs;
	}



}
