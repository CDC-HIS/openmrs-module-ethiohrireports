/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ethiohrireports.reports.converter;

import org.openmrs.Obs;
import org.openmrs.module.reporting.data.converter.DataConverter;

import java.util.List;
import java.util.stream.Collectors;

public class ObsListAsPipeDelimitedConverter implements DataConverter {
	
	public ObsListAsPipeDelimitedConverter() {
	}
	
	@Override
    public Object convert(Object o) {
        List<Obs> obs = (List<Obs>) o;
        if (obs != null) {
            return obs.stream().map(ob -> ob.getValueCoded().getName().getName())
                    .collect(Collectors.joining(" | "));
        }
        return null;
    }
	
	@Override
	public Class<?> getInputDataType() {
		return List.class;
	}
	
	@Override
	public Class<?> getDataType() {
		return String.class;
	}
}
