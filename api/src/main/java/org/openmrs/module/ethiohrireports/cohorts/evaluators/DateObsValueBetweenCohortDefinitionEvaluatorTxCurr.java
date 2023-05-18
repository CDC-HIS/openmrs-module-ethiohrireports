/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.ethiohrireports.cohorts.evaluators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.annotation.Handler;
import org.openmrs.module.reporting.cohort.definition.BaseObsCohortDefinition.TimeModifier;
import org.openmrs.api.context.Context;
import org.openmrs.module.ethiohrireports.cohorts.definition.DateObsValueBetweenCohortDefinitionTxCurr;
import org.openmrs.module.reporting.cohort.Cohorts;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.BaseObsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.BaseObsCohortDefinitionEvaluator;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.common.RangeComparator;
import org.openmrs.module.reporting.common.SetComparator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;

/**
 * Evaluates a DateObsValueBetweenCohortDefinition and produces a Cohort
 */
@Handler(supports = { DateObsValueBetweenCohortDefinitionTxCurr.class })
public class DateObsValueBetweenCohortDefinitionEvaluatorTxCurr extends BaseObsCohortDefinitionEvaluator {
	
	/**
	 * @see CohortDefinitionEvaluator#evaluate(org.openmrs.module.reporting.cohort.definition.CohortDefinition,
	 *      org.openmrs.module.reporting.evaluation.EvaluationContext)
	 * @should test any with many properties specified
	 * @should find nobody if no patients match
	 */
	public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) {
		DateObsValueBetweenCohortDefinitionTxCurr cd = (DateObsValueBetweenCohortDefinitionTxCurr) cohortDefinition;
		//cd.setTimeModifier(TimeModifier.LAST);
		Cohort c = this.getPatientsHavingObs(cd, null, null, null, null, cd.getOperator(), cd.getValueList(), context);
		return new EvaluatedCohort(c, cohortDefinition, context);
		//cd.getParameter("onOrAfter");
		
		/*Cohort c = Context.getService(CohortQueryService.class).getPatientsHavingRangedObs(
				TimeModifier.ANY, cd.getQuestion(), cd.getGroupingConcept(),
				null, null,
				cd.getLocationList(), cd.getEncounterTypeList(),
				RangeComparator.GREATER_EQUAL, cd.getOnOrAfter(),
				RangeComparator.LESS_EQUAL, cd.getOnOrBefore());*/
	}
	
	protected Cohort getPatientsHavingObs(BaseObsCohortDefinition cd, RangeComparator operator1, Object value1,
	        RangeComparator operator2, Object value2, SetComparator setOperator, List<? extends Object> valueList,
	        EvaluationContext context) {
		
		if (cd.getGroupingConcept() != null) {
			throw new RuntimeException("grouping concept not yet implemented");
		}
		
		boolean joinOnEncounter = cd.getEncounterTypeIds() != null;
		String dateAndLocationSql = ""; // TODO rename to include encounterType
		String dateAndLocationSqlForSubquery = "";
		// if (cd.getOnOrAfter() != null) {
		// 	dateAndLocationSql += " and o.value_datetime >= :onOrAfter ";
		// 	dateAndLocationSqlForSubquery += " and obs.value_datetime >= :onOrAfter ";
		// }
		if (cd.getOnOrBefore() != null) {
			dateAndLocationSql += " and o.value_datetime > :onOrBefore ";
			dateAndLocationSqlForSubquery += " and obs.value_datetime > :onOrBefore ";
		}
		if (cd.getLocationIds() != null) {
			dateAndLocationSql += " and o.location_id in (:locationIds) ";
			dateAndLocationSqlForSubquery += " and obs.location_id in (:locationIds) ";
		}
		if (cd.getEncounterTypeIds() != null) {
			dateAndLocationSql += " and e.encounter_type in (:encounterTypeIds) ";
			dateAndLocationSqlForSubquery += " and encounter.encounter_type in (:encounterTypeIds) ";
		}
		
		TimeModifier tm = cd.getTimeModifier();
		if (tm == null) {
			tm = TimeModifier.ANY;
		}
		boolean doSqlAggregation = tm == TimeModifier.MIN || tm == TimeModifier.MAX || tm == TimeModifier.AVG;
		boolean doInvert = tm == TimeModifier.NO;
		
		String valueSql = null;
		List<String> valueClauses = new ArrayList<String>();
		List<Object> valueListForQuery = null;
		
		if (value1 != null || value2 != null) {
			valueSql = (value1 != null && value1 instanceof Number) ? " o.value_numeric " : " o.value_datetime ";
		} else if (valueList != null && valueList.size() > 0) {
			valueListForQuery = new ArrayList<Object>();
			if (valueList.get(0) instanceof String) {
				valueSql = " o.value_text ";
				for (Object o : valueList) {
					valueListForQuery.add(o);
				}
			} else {
				valueSql = " o.value_coded ";
				for (Object o : valueList) {
					if (o instanceof Concept) {
						valueListForQuery.add(((Concept) o).getConceptId());
					} else if (o instanceof Number) {
						valueListForQuery.add(((Number) o).intValue());
					} else {
						throw new IllegalArgumentException("Don't know how to handle " + o.getClass() + " in valueList");
					}
				}
			}
		}
		
		if (doSqlAggregation) {
			valueSql = " " + tm.toString() + "(" + valueSql + ") ";
		}
		
		if (value1 != null || value2 != null) {
			if (value1 != null) {
				valueClauses.add(valueSql + operator1.getSqlRepresentation() + " :value1 ");
			}
			if (value2 != null) {
				valueClauses.add(valueSql + operator2.getSqlRepresentation() + " :value2 ");
			}
		} else if (valueList != null && valueList.size() > 0) {
			valueClauses.add(valueSql + setOperator.getSqlRepresentation() + " (:valueList) ");
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(" With ob As( ");
		sql.append(" SELECT * FROM (select *, ROW_NUMBER() OVER (PARTITION BY person_id ORDER BY value_datetime DESC) AS ROWNUM from obs ");
		if (cd.getQuestion() != null) {
			sql.append(" where concept_id = :questionConceptId ");
		}
		sql.append(" ) AS ob1 ");
		sql.append(" where ob1.ROWNUM = 1)");
		
		sql.append(" select o.person_id from ob AS o ");
		
		// sql.append(" select o.person_id from obs o ");
		sql.append(" inner join patient p on o.person_id = p.patient_id ");
		if (joinOnEncounter) {
			sql.append(" inner join encounter e on o.encounter_id = e.encounter_id ");
		}
		// o.voided = false and 
		if (tm == TimeModifier.ANY || tm == TimeModifier.NO) {
			sql.append(" where p.voided = false ");
			if (cd.getQuestion() != null) {
				sql.append(" and concept_id = :questionConceptId ");
			}
			sql.append(dateAndLocationSql);
		} else if (tm == TimeModifier.FIRST || tm == TimeModifier.LAST) {
			boolean isFirst = tm == TimeModifier.FIRST;
			sql.append(" inner join ( ");
			sql.append("    select person_id, " + (isFirst ? "MIN" : "MAX") + "(value_datetime) as odt ");
			sql.append("    from obs ");
			if (joinOnEncounter) {
				sql.append(" inner join encounter on obs.encounter_id = encounter.encounter_id ");
			}
			sql.append("             where obs.voided = false and obs.concept_id = :questionConceptId "
			        + dateAndLocationSqlForSubquery + " group by person_id ");
			sql.append(" ) subq on o.person_id = subq.person_id and o.value_datetime = subq.odt ");
			sql.append(" where o.voided = false and p.voided = false and o.concept_id = :questionConceptId ");
			sql.append(dateAndLocationSql);
		} else if (doSqlAggregation) {
			sql.append(" where o.voided = false and p.voided = false and concept_id = :questionConceptId "
			        + dateAndLocationSql);
			sql.append(" group by o.person_id ");
		} else {
			throw new IllegalArgumentException("TimeModifier '" + tm + "' not recognized");
		}
		
		if (valueClauses.size() > 0) {
			sql.append(doSqlAggregation ? " having " : " and ");
			for (Iterator<String> i = valueClauses.iterator(); i.hasNext();) {
				sql.append(i.next());
				if (i.hasNext()) {
					sql.append(" and ");
				}
			}
		}
		
		log.debug("sql: " + sql);
		System.out.println("Sql HtsNew Report: " + sql);
		
		SqlQueryBuilder qb = new SqlQueryBuilder();
		qb.append(sql.toString());
		
		if (cd.getQuestion() != null) {
			qb.addParameter("questionConceptId", cd.getQuestion().getConceptId());
		}
		if (value1 != null) {
			qb.addParameter("value1", value1);
		}
		if (value2 != null) {
			qb.addParameter("value2", value2);
		}
		if (valueListForQuery != null) {
			qb.addParameter("valueList", valueListForQuery);
		}
		if (cd.getOnOrAfter() != null) {
			qb.addParameter("onOrAfter", cd.getOnOrAfter());
		}
		if (cd.getOnOrBefore() != null) {
			qb.addParameter("onOrBefore", DateUtil.getEndOfDayIfTimeExcluded(cd.getOnOrBefore()));
		}
		if (cd.getLocationIds() != null) {
			qb.addParameter("locationIds", cd.getLocationIds());
		}
		if (cd.getEncounterTypeIds() != null) {
			qb.addParameter("encounterTypeIds", cd.getEncounterTypeIds());
		}
		
		System.out.println("query builder" + qb);
		List<Integer> ids = Context.getService(EvaluationService.class).evaluateToList(qb, Integer.class, context);
		if (doInvert) {
			Set<Integer> inverted = Cohorts.allPatients(context).getMemberIds();
			inverted.removeAll(ids);
			return new Cohort(inverted);
		} else {
			return new Cohort(ids);
		}
	}
	
}
