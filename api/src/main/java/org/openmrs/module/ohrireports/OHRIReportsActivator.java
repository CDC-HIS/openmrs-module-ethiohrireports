/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ohrireports;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.ohrireports.api.task.FlattenTableTask;
import org.openmrs.module.ohrireports.cohorts.util.HtsStaticCohortsUtil;
import org.openmrs.module.reporting.report.manager.ReportManager;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;

import static org.openmrs.module.ohrireports.cohorts.manager.CohortDefinitionManagerUtil.setUpCohortsDefinitions;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class OHRIReportsActivator extends BaseModuleActivator {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * @see #started()
	 */
	public void started() {
		log.info("Started ETHIOHRI Reports");
		String taskName = "Mamba - database Flattening Task";
		Long repeatInterval = 300L; //second
		String taskClassName = FlattenTableTask.class.getName();
		String description = "Mamba - Flatten the OpenMRS data-models (Database) Task";
		
		addTask(taskName, taskClassName, repeatInterval, description);
		setUpCohortsDefinitions();
		
		for (ReportManager reportManager : Context.getRegisteredComponents(ReportManager.class)) {
			log.info("Setting up report " + reportManager.getName() + "...");
			ReportManagerUtil.setupReport(reportManager);
		}
		
		log.info("Setting up HTS static cohorts...");
		HtsStaticCohortsUtil.setupHtsCohorts();
	}
	
	/**
	 * @see #shutdown()
	 */
	public void shutdown() {
		log.info("Shutdown ETHIOHRI Reports");
	}
	
	void addTask(String name, String className, Long repeatInterval, String description) {
		
		SchedulerService scheduler = Context.getSchedulerService();
		TaskDefinition taskDefinition = scheduler.getTaskByName(name);
		if (taskDefinition == null) {
			
			taskDefinition = new TaskDefinition(null, name, description, className);
			taskDefinition.setStartOnStartup(Boolean.TRUE);
			taskDefinition.setRepeatInterval(repeatInterval);
			scheduler.saveTaskDefinition(taskDefinition);
		}
	}
	
}
