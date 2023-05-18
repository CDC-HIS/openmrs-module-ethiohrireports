package org.openmrs.module.ohrireports.reports.datasetevaluator;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.ALIVE;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.MRN_PATIENT_IDENTIFIERS;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.OPENMRS_PATIENT_IDENTIFIERS;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.PATIENT_STATUS;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.REGIMEN;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.RESTART;
import static org.openmrs.module.ohrireports.OHRIReportsConstants.TREATMENT_END_DATE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Person;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.api.PatientService;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.ohrireports.reports.datasetdefinition.TXCurrDataSetDefinition;
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
	@Autowired
	private DbSessionFactory sessionFactory;

	private TXCurrDataSetDefinition hdsd;

	private EvaluationContext context;
	HashMap<Integer, Concept> patientStatus = new HashMap<>();

	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext)
			throws EvaluationException {

		hdsd = (TXCurrDataSetDefinition) dataSetDefinition;
		context = evalContext;
		SimpleDataSet data = new SimpleDataSet(dataSetDefinition, evalContext);


		DataSetRow row = null;


		List<Object[]> resultSet = getEtlCurr();

		for (Object[] objects : resultSet) {

			 //EthiopianDate ethiopianDate = null;
			try {
				// ethiopianDate = EthiopianDateConverter
				 //.ToEthiopianDate(DateTime.parse(objects[2]
				// .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

				row = new DataSetRow();
				row.addColumnValue(new DataSetColumn("identifier", "OpenmrsID",
						String.class),
						Objects.isNull(objects[0]) || Objects.isNull(objects[0]) ? "" : objects[0].toString());

				row.addColumnValue(new DataSetColumn("full_name", "FullName",
						String.class),
						Objects.isNull(objects[1]) || Objects.isNull(objects[1]) ? "" : objects[1].toString());

				row.addColumnValue(new DataSetColumn("gender", "Gender", String.class),
						objects[2]);

				row.addColumnValue(new DataSetColumn("age", "Age",
						Date.class), objects[3]);
				row.addColumnValue(new DataSetColumn("treatment_end_date", "Treatment End Date",
						Date.class), objects[4]);
				// row.addColumnValue(new DataSetColumn("TreatmentEndDateETC", "Treatment End
				// Date ETH",
				// String.class),
				// ethiopianDate.equals(null) ? ""
				// : ethiopianDate.getDay() + "/" + ethiopianDate.getMonth() + "/"
				// + ethiopianDate.getYear());

				row.addColumnValue(new DataSetColumn("patient_status", "Status",
				Date.class), objects[5]);
				row.addColumnValue(new DataSetColumn("regiment", "Regiment",
				Date.class), objects[6]);
				data.addRow(row);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return data;
	}

	public DbSessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private List<Object[]> getEtlCurr() {
		SQLQuery txCurrQuery = sessionFactory.getCurrentSession()
				.createSQLQuery("CALL sp_fact_tx_curr_query(:art_end_date)");

		txCurrQuery.setParameter("art_end_date", hdsd.getEndDate());
		txCurrQuery.addScalar("identifier", StandardBasicTypes.STRING);
		txCurrQuery.addScalar("full_name", StandardBasicTypes.STRING);
		txCurrQuery.addScalar("gender", StandardBasicTypes.STRING);
		txCurrQuery.addScalar("age", StandardBasicTypes.INTEGER);
		txCurrQuery.addScalar("treatment_end_date", StandardBasicTypes.DATE);
		txCurrQuery.addScalar("patient_status", StandardBasicTypes.STRING);
		txCurrQuery.addScalar("regiment", StandardBasicTypes.STRING);

		List<Object[]> txCurrResult = txCurrQuery.list();
		return txCurrResult;
	}

}
