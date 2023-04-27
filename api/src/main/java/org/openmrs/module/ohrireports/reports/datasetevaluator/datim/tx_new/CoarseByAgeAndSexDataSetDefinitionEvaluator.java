package org.openmrs.module.ohrireports.reports.datasetevaluator.datim.tx_new;

import static org.openmrs.module.ohrireports.OHRIReportsConstants.ART_START_DATE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ConceptService;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.tx_new.AutoCalculateDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.tx_new.CoarseByAgeAndSexDataSetDefinition;
import org.openmrs.module.ohrireports.reports.datasetdefinition.datim.tx_new.FineByAgeAndSexDataSetDefinition;
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

@Handler(supports = { CoarseByAgeAndSexDataSetDefinition.class })
public class CoarseByAgeAndSexDataSetDefinitionEvaluator implements DataSetEvaluator {

        private EvaluationContext context;

        private CoarseByAgeAndSexDataSetDefinition hdsd;
        private Concept artConcept;
        private int total = 0;
        @Autowired
        private ConceptService conceptService;

        @Autowired
        private EvaluationService evaluationService;
       
        List<Obs> obses = new ArrayList<>();

        @Override
        public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext)
                        throws EvaluationException {
                        total=0;
                hdsd = (CoarseByAgeAndSexDataSetDefinition) dataSetDefinition;
                context = evalContext;
                artConcept = conceptService.getConceptByUuid(ART_START_DATE);
                SimpleDataSet set = new SimpleDataSet(dataSetDefinition, evalContext);
                DataSetRow femaleDateSet = new DataSetRow();
                obses = LoadObs("F");
                femaleDateSet.addColumnValue(new DataSetColumn("FineByAgeAndSexData", "Gender",
                                Integer.class), "Female");
                femaleDateSet.addColumnValue(new DataSetColumn("unkownAge", "Unkown Age", Integer.class),
                               getEnrolledByUnknownAge());
                femaleDateSet.addColumnValue(new DataSetColumn("<15", "<15", Integer.class),
                                getEnrolledByAgeAndGender(0, 15));
                femaleDateSet.addColumnValue(new DataSetColumn("15+", "15+", Integer.class),
                                getEnrolledByAgeAndGender(15, 200));

                set.addRow(femaleDateSet);

                obses = LoadObs("M");

                DataSetRow maleDataSet = new DataSetRow();
                maleDataSet.addColumnValue(new DataSetColumn("FineByAgeAndSexData", "Gender",
                                Integer.class), "Male");
                maleDataSet.addColumnValue(new DataSetColumn("unkownAge", "Unkown Age", Integer.class),
                             getEnrolledByUnknownAge());

                maleDataSet.addColumnValue(new DataSetColumn("<15", "<15", Integer.class),
                                getEnrolledByAgeAndGender(0, 15));
                maleDataSet.addColumnValue(new DataSetColumn("15+", "15+", Integer.class),
                                getEnrolledByAgeAndGender(15, 200));

                set.addRow(maleDataSet);
                DataSetRow tSetRow = new DataSetRow();
                tSetRow.addColumnValue(new DataSetColumn("subtotal", "Sub-Total", Integer.class),
                                total);
                set.addRow(tSetRow);
                return set;
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

        private int getEnrolledByAgeAndGender(int min, int max) {
                int count = 0;
                List<Integer> personIds = new ArrayList<>();
                for (Obs obs : obses) {
                        if (personIds.contains(obs.getPersonId()))
                                continue;
                      
                                if (obs.getPerson().getAge() >= min &&
                         obs.getPerson().getAge() <= max) {
                                personIds.add(obs.getPersonId());
                                count++;
                        }

                }
                incrementTotalCount(count);
                clearCountedPerson(personIds);
                return count;
        }


        private void incrementTotalCount(int count)
        {
                if(count>0)
                total=total+count;
        }

        private void clearCountedPerson(List<Integer> personIds) {
                for (int pId : personIds) {
                        obses.removeIf(p -> p.getPersonId().equals(pId));
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
