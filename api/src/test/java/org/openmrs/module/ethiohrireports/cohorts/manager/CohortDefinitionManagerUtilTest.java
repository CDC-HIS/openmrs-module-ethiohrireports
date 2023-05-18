package org.openmrs.module.ethiohrireports.cohorts.manager;

import org.junit.Assert;
import org.junit.Test;

import static org.openmrs.module.ethiohrireports.cohorts.manager.CohortDefinitionManagerUtil.isSnapshotOrGreater;

public class CohortDefinitionManagerUtilTest {
	
	@Test
	public void isSnapshotOrGreater_shouldReturnTrueIfSnapshotVersion() {
		// replay and verify
		Assert.assertTrue(isSnapshotOrGreater("0.2-SNAPSHOT", "0.2-SNAPSHOT"));
	}
	
	@Test
	public void isSnapshotOrGreater_shouldReturnTrueIfIncomingIsGreater() {
		// replay and verify
		Assert.assertTrue(isSnapshotOrGreater("0.2", "0.3"));
	}
	
	@Test
	public void isSnapshotOrGreater_shouldReturnFalseIfIncomingIsLowerVersion() {
		// replay and verify
		Assert.assertFalse(isSnapshotOrGreater("0.2", "0.1"));
	}
}
