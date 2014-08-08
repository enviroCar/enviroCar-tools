/**
 * Copyright (C) 2014
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.envirocar.analyse.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryPoint implements Point {

	private String id;
	private double x;
	private double y;
	private Map<String, Object> propertyMap;
	private Map<String, Integer> propertyPointsUsedForAggregationMap;
	private int numberOfPointsUsedForAggregation = 1;
	private List<String> tracksUsedForAggregation;
	private int numberOfTracksUsedForAggregation;
	private String lastContributingTrack;
	
	public InMemoryPoint(String id, double x, double y, Map<String, Object> propertyMap, int numberOfPointsUsedForAggregation, int numberOfTracksUsedForAggregation, String lastContributingTrack, Map<String, Integer> propertyPointsUsedForAggregationMap){
		this.id = id;
		this.x = x;
		this.y = y;	
		this.propertyMap = propertyMap;		
		this.numberOfPointsUsedForAggregation = numberOfPointsUsedForAggregation;	
		this.numberOfTracksUsedForAggregation = numberOfTracksUsedForAggregation;	
		this.lastContributingTrack = lastContributingTrack;	
		this.propertyPointsUsedForAggregationMap = propertyPointsUsedForAggregationMap;
	}
	
	public InMemoryPoint(Point otherPoint){
		this.id = otherPoint.getID();
		this.x = otherPoint.getX();
		this.y = otherPoint.getY();
		this.propertyMap = otherPoint.getPropertyMap();	
		this.numberOfPointsUsedForAggregation = otherPoint.getNumberOfPointsUsedForAggregation();	
		this.numberOfTracksUsedForAggregation = otherPoint.getNumberOfTracksUsedForAggregation();	
		this.lastContributingTrack = otherPoint.getLastContributingTrack();
		tracksUsedForAggregation = otherPoint.getTracksUsedForAggregation();
		this.propertyPointsUsedForAggregationMap = otherPoint.getPropertyPointsUsedForAggregationMap();
	}
	
	public InMemoryPoint(){
		
	}
	
	@Override
	public String getID() {
		return id;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public int getNumberOfPointsUsedForAggregation() {
		return numberOfPointsUsedForAggregation;
	}

	@Override
	public List<String> getTracksUsedForAggregation() {
		return tracksUsedForAggregation;
	}

	@Override
	public void setNumberOfPointsUsedForAggregation(int numberOfPoints) {
		this.numberOfPointsUsedForAggregation = numberOfPoints;
	}

	@Override
	public void addTrackUsedForAggregation(String trackID) {
		tracksUsedForAggregation.add(trackID);
	}

	@Override
	public Map<String, Object> getPropertyMap() {
		return propertyMap;
	}

	@Override
	public void setProperty(String propertyName, Object value) {
		propertyMap.put(propertyName, value);		
	}

	@Override
	public Object getProperty(String propertyName) {		
		return propertyMap.get(propertyName);
	}

	@Override
	public void setID(String id) {
		this.id = id;
	}
	
	@Override
	public int getNumberOfTracksUsedForAggregation() {
		return numberOfTracksUsedForAggregation;
	}

	@Override
	public String getLastContributingTrack() {
		return lastContributingTrack;
	}

	@Override
	public void setNumberOfTracksUsedForAggregation(
			int numberOfTracksUsedForAggregation) {
		this.numberOfTracksUsedForAggregation = numberOfTracksUsedForAggregation;
	}
	
	@Override
	public void setLastContributingTrack(String lastContributingTrack) {
		this.lastContributingTrack = lastContributingTrack;
	}

	@Override
	public void setX(double x) {
		this.x = x;		
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public int getNumberOfPointsUsedForAggregation(String propertyName) {

		if (propertyPointsUsedForAggregationMap.containsKey(propertyName)) {
			return propertyPointsUsedForAggregationMap.get(propertyName);
		}
		return 1;
	}

	@Override
	public void setNumberOfPointsUsedForAggregation(int numberOfPoints,
			String propertyName) {
		propertyPointsUsedForAggregationMap.put(propertyName, numberOfPoints);		
	}

	@Override
	public Map<String, Integer> getPropertyPointsUsedForAggregationMap() {		
		return propertyPointsUsedForAggregationMap;
	}

}