// package org.openmrs.module.ohrireports.reports.query.evaluators;

// import org.openmrs.Obs;
// import org.openmrs.annotation.Handler;
// import org.openmrs.module.ohrireports.reports.query.ObsEncounterQuery;
// import org.openmrs.module.reporting.common.ObjectUtil;
// import org.openmrs.module.reporting.evaluation.EvaluationContext;
// import org.openmrs.module.reporting.evaluation.EvaluationException;
// import org.openmrs.module.reporting.evaluation.querybuilder.HqlQueryBuilder;
// import org.openmrs.module.reporting.evaluation.service.EvaluationService;
// import org.openmrs.module.reporting.query.encounter.EncounterQueryResult;
// import org.openmrs.module.reporting.query.encounter.definition.EncounterQuery;
// import org.openmrs.module.reporting.query.encounter.evaluator.EncounterQueryEvaluator;
// import org.springframework.beans.factory.annotation.Autowired;

// @Handler(supports = ObsEncounterQuery.class)
// public class ObsEncounterQueryEvaluator implements EncounterQueryEvaluator {

// 	@Autowired
// 	EvaluationService evaluationService;

// 	@Override
// 	public EncounterQueryResult evaluate(EncounterQuery definition, EvaluationContext context) throws EvaluationException {

// 		ObsEncounterQuery oq = (ObsEncounterQuery) definition;
// 		context = ObjectUtil.nvl(context, new EvaluationContext());
// 		EncounterQueryResult result = new EncounterQueryResult(oq, context);

// 		HqlQueryBuilder q = new HqlQueryBuilder();
// 		q.select("obs.encounter.encounterId");
// 		q.from(Obs.class, "obs");
// 		q.whereEqual("obs.concept", oq.getQuestion());
// 		q.whereIn("obs.encounter.encounterType", oq.getEncounterTypes());
// 		q.whereGreaterOrEqualTo("obs.valueDatetime", oq.getObsValueOnOrAfter());
// 		q.whereLessOrEqualTo("obs.valueDatetime", oq.getObsValueOnOrBefore());
// 		q.whereIn("obs.encounter.location", oq.getEncounterLocations());
// 		q.whereEncounterIn("obs.encounter.encounterId", context);

// 		result.addAll(evaluationService.evaluateToList(q, Integer.class, context));
// 		return result;
// 	}

// }
