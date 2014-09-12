/**
 * Copyright (C) 2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.envirocar.analyse;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.envirocar.harvest.ProgressListener;
import org.envirocar.harvest.TrackHarvester;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AggregateAllTracksTest {

	private static final Logger logger = LoggerFactory.getLogger(AggregateAllTracksTest.class);
	
	@Test
	public void harvest() throws ClientProtocolException, IOException {
		final AggregationAlgorithm algo = new AggregationAlgorithm();
		TrackHarvester harv = new TrackHarvester("", new ProgressListener() {
			@Override
			public void onProgressUpdate(float progressPercent) {
				logger.info("Progress: "+progressPercent);
			}
		}) {
			@Override
			public void readAndPushTrack(String id)
					throws ClientProtocolException, IOException {
				try {
					algo.runAlgorithm(id);
				} 
				catch (IOException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		};
		
		harv.harvestTracks();
	}
	
}
