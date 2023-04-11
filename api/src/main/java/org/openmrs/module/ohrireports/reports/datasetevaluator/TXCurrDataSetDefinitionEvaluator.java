package org.openmrs.module.ohrireports.reports.datasetevaluator;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.ALIVE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.PATIENT_STATUS;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.REGIMEN;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.RESTART;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.MRN_PATIENT_IDENTIFIERS;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TREATMENT_END_DATE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.OPENMRS_PATIENT_IDENTIFIERS;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Person;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.api.PatientService;
import org.openmrs.module.ohrireports.helper.EthiopianDate;
import org.openmrs.module.ohrireports.helper.EthiopianDateConverter;
import org.openmrs.module.ohrireports.reports.datasetdefinition.TXCurrDataSetDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientObjectDataDefinition;
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

@Handler(supports = { TXCurrDataSetDefinition.class })
public class TXCurrDataSetDefinitionEvaluator implements DataSetEvaluator {

	@Autowired
	EvaluationService evaluationService;

	@Autowired
	ConceptService conceptService;

	@Autowired
	PatientService patientService;

	private TXCurrDataSetDefinition hdsd;
	private EvaluationContext context;
	HashMap<Integer, Concept> patientStatus = new HashMap<>();

	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext)
			throws EvaluationException {

		hdsd = (TXCurrDataSetDefinition) dataSetDefinition;
		context = evalContext;
		SimpleDataSet data = new SimpleDataSet(dataSetDefinition, evalContext);

		List<Obs> obsList = getTxCurrPatients();

		DataSetRow row = null;
		PatientIdentifierType mrnIdentifierType = patientService
				.getPatientIdentifierTypeByUuid(MRN_PATIENT_IDENTIFIERS);
		PatientIdentifierType openmrsIdentifierType = patientService
				.getPatientIdentifierTypeByUuid(OPENMRS_PATIENT_IDENTIFIERS);

		for (Obs obses : obsList) {

			Person person = obses.getPerson();
			// row should be filled with only patient data
			if (!person.getIsPatient())
				continue;

			Patient patient = patientService.getPatient(person.getId());
			
			
			Concept status = patientStatus.get(person.getId());
			EthiopianDate ethiopianDate = null;
			try {
				ethiopianDate = EthiopianDateConverter
						.ToEthiopianDate(obses.getValueDate()
						.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			
			row = new DataSetRow();
			row.addColumnValue(new DataSetColumn("MRN", "MRN", String.class),
			getStringIdentifier(patient.getPatientIdentifier(mrnIdentifierType)));
			
			row.addColumnValue(new DataSetColumn("OpenMrs", "Openmrs ID", String.class),
			getStringIdentifier(patient.getPatientIdentifier(openmrsIdentifierType)) );
			
			row.addColumnValue(new DataSetColumn("Name", "Name", String.class), person.getNames());
			
			row.addColumnValue(new DataSetColumn("Age", "Age", Integer.class), person.getAge());
			
			row.addColumnValue(new DataSetColumn("Gender", "Gender", String.class), person.getGender());
			
			row.addColumnValue(new DataSetColumn("TreatmentEndDate", "Treatment End Date",
					Date.class), obses.getValueDate());
			row.addColumnValue(new DataSetColumn("TreatmentEndDateETC", "Treatment End Date ETH",
					String.class),
					ethiopianDate.equals(null) ? ""
							: ethiopianDate.getDay() + "/" + ethiopianDate.getMonth() + "/" + ethiopianDate.getYear());
			row.addColumnValue(new DataSetColumn("Regimen", "Regmin", String.class), getRegimen(obses, evalContext));
			
			row.addColumnValue(new DataSetColumn("Status", "Status",
					String.class),
					Objects.isNull(status) || Objects.isNull(status.getName()) ? "" : status.getName().getName());
			data.addRow(row);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		}
		return data;
	}

	

	private String getStringIdentifier(PatientIdentifier patientIdentifier) {
		return Objects.isNull(patientIdentifier)?"--":patientIdentifier.getIdentifier();
	}



	private List<Obs> getTxCurrPatients() {

		List<Integer> patientsId = getListOfALiveORRestartPatients();

		List<Person> patients = new ArrayList<>();
		List<Obs> obseList = new ArrayList<>();

		if (patientsId == null || patientsId.size() == 0)
			return obseList;
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();
		queryBuilder.select("obv");
		queryBuilder.from(Obs.class, "obv")
				.whereEqual("obv.encounter.encounterType", hdsd.getEncounterType())
				.and()
				.whereEqual("obv.concept", conceptService.getConceptByUuid(TREATMENT_END_DATE))
				.and()
				.whereGreaterOrEqualTo("obv.valueDatetime", hdsd.getEndDate())
				.and()
				.whereLess("obv.obsDatetime", hdsd.getEndDate())
				.whereIdIn("obv.personId", patientsId)
				.orderDesc("obv.personId,obv.obsDatetime");
		System.out.println(queryBuilder.toString());
		List<Obs> evaluateToList = evaluationService.evaluateToList(queryBuilder, Obs.class, context);
		for (Obs obs : evaluateToList) {
			if (!patients.contains(obs.getPerson())) {
				patients.add(obs.getPerson());
				obseList.add(obs);
			}
		}
		return obseList;
	}

	private List<Integer> getListOfALiveORRestartPatients() {

		List<Concept> concepts = Arrays.asList(conceptService.getConceptByUuid(ALIVE),
				conceptService.getConceptByUuid(RESTART));

		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();

		queryBuilder.select(" obv")

				.from(Obs.class, "obv")
				.whereEqual("obv.encounter.encounterType", hdsd.getEncounterType())
				.and()
				.whereEqual("obv.concept", conceptService.getConceptByUuid(PATIENT_STATUS))
				.and()
				.whereIn("obv.valueCoded", concepts);

		Set<Integer> patients = new LinkedHashSet<>();
		List<Obs> obsList=  evaluationService.evaluateToList(queryBuilder, Obs.class, context);
		//Removing duplicate using HashSet
		 for (Obs obs : obsList) {
			if(patients.add(obs.getPersonId()))
			{
				//updating 
				patientStatus.put(obs.getPersonId(), obs.getValueCoded());
			}
		 }
		
		 return new ArrayList<Integer>(patients);
	}

	private String getRegimen(Obs obs, EvaluationContext context) {
		HqlQueryBuilder queryBuilder = new HqlQueryBuilder();

		queryBuilder.select("obv.valueCoded").from(Obs.class, "obv")
				.whereInAny("obv.concept", conceptService.getConceptByUuid(REGIMEN))
				.whereEqual("obv.encounter", obs.getEncounter())
				.and().whereEqual("obv.person", obs.getPerson())
				.orderDesc("obv.obsDatetime").limit(1);
		List<Concept> concepts = evaluationService.evaluateToList(queryBuilder, Concept.class, context);

		Concept data = null;
		if (concepts != null && concepts.size() > 0)
			data = concepts.get(0);

		return data == null ? "--" : data.getName().getName();
	}
}
