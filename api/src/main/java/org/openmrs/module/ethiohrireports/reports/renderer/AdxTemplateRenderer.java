/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ethiohrireports.reports.renderer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.openmrs.annotation.Handler;
import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.indicator.dimension.CohortIndicatorAndDimensionResult;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.ReportDesignResource;
import org.openmrs.module.reporting.report.renderer.RenderingException;
import org.openmrs.module.reporting.report.renderer.ReportTemplateRenderer;
import org.xml.sax.InputSource;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Report Renderer implementation that supports rendering of an adx template
 */
@Handler
@Localized("reporting.AdxTemplateRenderer")
public class AdxTemplateRenderer extends ReportTemplateRenderer {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	public AdxTemplateRenderer() {
		super();
	}
	
	@Override
	public void render(ReportData reportData, String s, OutputStream outputStream) throws RenderingException {
		log.debug("Attempting to render report with AdxTemplateRenderer");
		
		Writer pw = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
		
		try {
			ReportDesign reportDesign = getDesign(s);
			ReportDesignResource reportDesignResource = getTemplate(reportDesign);
			SAXReader saxReader = new SAXReader();
			String xml = new String(reportDesignResource.getContents(), StandardCharsets.UTF_8);
			Document document = saxReader.read(new InputSource(new StringReader(xml)));
			Element root = document.getRootElement();
			Element group = root.element("group");
			List<Node> nodes = group.elements();
			for (Node node : nodes) {
				Element el = (Element) node;
				Attribute attr = el.attribute("value");
				if (attr != null) {
					String val = attr.getStringValue();
					String replacement = getReplacement(reportData, val);
					el.addAttribute("value", replacement);
				}
				
			}
			pw.write(document.asXML());
		}
		catch (RenderingException re) {
			throw re;
		}
		catch (Throwable e) {
			throw new RenderingException("Unable to render results due to: " + e, e);
		}
		finally {
			IOUtils.closeQuietly(pw);
		}
	}
	
	private String getReplacement(ReportData reportData, String initial) {
		Map<String, DataSet> datasets = reportData.getDataSets();
		String replacement = initial;
		Iterator<Map.Entry<String, DataSet>> datasetsItr = datasets.entrySet().iterator();
		while (datasetsItr.hasNext()) {
			DataSet adx = datasetsItr.next().getValue();
			Iterator<DataSetRow> it = adx.iterator();
			while (it.hasNext()) {
				DataSetRow dsr = it.next();
				CohortIndicatorAndDimensionResult ciadr = (CohortIndicatorAndDimensionResult) dsr.getColumnValue(StringUtils
				        .replace(initial, "#", ""));
				if (ciadr != null) {
					Number colValue = ciadr.getValue();
					if (String.valueOf(colValue) != initial) {
						replacement = String.valueOf(colValue);
					}
				}
			}
		}
		return replacement;
	}
}
