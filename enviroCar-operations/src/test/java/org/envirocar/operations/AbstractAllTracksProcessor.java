/**
 * Copyright (C) 2015 52°North Initiative for Geospatial Open Source
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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.envirocar.operations;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.http.client.ClientProtocolException;
import org.envirocar.harvest.ProgressListener;
import org.envirocar.harvest.TrackHarvester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author matthes
 */
public abstract class AbstractAllTracksProcessor {
    
     
    private static final Logger logger = LoggerFactory.getLogger(AggregateAllTracks.class);
    private boolean withinTrackIdWindow = true;
    private String firstTrackId;
    private String lastTrackId;
    private Set<String> trackIds = new HashSet<>();
    
    protected void process(final SingleTrackHandler sth) throws ClientProtocolException, IOException {
        process(sth, "");
    }
    
    
    protected void process(String targetUrl) throws IOException {
        process(null, targetUrl);
    }
    
    protected void process(final SingleTrackHandler sth, final String targetUrl) throws ClientProtocolException, IOException {
        firstTrackId = System.getProperty("all.tracks.firstid");
        lastTrackId = System.getProperty("all.tracks.lastid");
        String trackIdList = System.getProperty("all.tracks.list");
        if (trackIdList != null && !trackIdList.isEmpty()) {
            trackIds.addAll(Arrays.asList(trackIdList.split(",")));
        }
        else {
            logger.info("You can provide a list of track ids using the 'all.tracks.list' property, seperated by commas");
        }
        
        if (firstTrackId == null || firstTrackId.isEmpty()) {
            logger.info("You can provide the first track id to be considered via the 'all.tracks.firstid' property");
        }
        else {
            withinTrackIdWindow = false;
        }
        
        if (lastTrackId == null || lastTrackId.isEmpty()) {
            logger.info("You can provide the last track id to be considered via the 'all.tracks.lastid' property");
        }
        
        TrackHarvester harv;
        if (trackIds.isEmpty()) {
            harv = new WindowedHarvester(targetUrl, sth);
        }
        else {
            harv = new IdBasedListener(targetUrl, sth, trackIds);
        }
        
        harv.harvestTracks();
    }
    
    private class IdBasedListener extends TrackHarvester {
        private final Set<String> ids;
        private final SingleTrackHandler sth;
        
        public IdBasedListener(String consumerUrl, final SingleTrackHandler sth, Set<String> ids) {
            super(consumerUrl, new ProgressListener() {
            @Override
            public void onProgressUpdate(float progressPercent) {
                logger.info("Progress: "+progressPercent);
            }});
            
            this.ids = ids;
            this.sth = sth;
        }
        
        @Override
        public void readAndPushTrack(String id)
                throws ClientProtocolException, IOException {
            if (!ids.contains(id)) {
                logger.info("ID not in set. skipping: "+ id);
                return;
            }

            if (sth == null)  {
                /*
                * apply the default method as we do not have a handler
                */
                super.readAndPushTrack(id);
            }
            else {
                try {
                    sth.actOnTrack(id);
                }
                catch (IOException e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        
    }

    private class WindowedHarvester extends TrackHarvester {
        private final SingleTrackHandler sth;

        public WindowedHarvester(String consumerUrl, final SingleTrackHandler sth) {
            super(consumerUrl, new ProgressListener() {
            @Override
            public void onProgressUpdate(float progressPercent) {
                logger.info("Progress: "+progressPercent);
            }});
            
            this.sth = sth;
        }
        
        @Override
        public void readAndPushTrack(String id)
                throws ClientProtocolException, IOException {
            if (!withinTrackIdWindow) {
                if (id.equals(firstTrackId)) {
                    withinTrackIdWindow = true;
                } else {
                    logger.info("skipping trackid: "+ id);
                    return;
                }
            }

            if (sth == null)  {
                /*
                * apply the default method as we do not have a handler
                */
                super.readAndPushTrack(id);
            }
            else {
                try {
                    sth.actOnTrack(id);
                }
                catch (IOException e) {
                    logger.warn(e.getMessage(), e);
                }
            }

            if (id.equals(lastTrackId)) {
                logger.info("reached last trackid: "+ id);
                withinTrackIdWindow = false;
            }
        }
    }
    
}
