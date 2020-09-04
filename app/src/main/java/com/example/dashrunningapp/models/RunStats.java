package com.example.dashrunningapp.models;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

//Container for running stats values
public class RunStats {
    private float totalDistance;
    private Double avgElevation;
    private List<Time> elevationDistance;
    private List<Time> distanceTimes;

    public RunStats(){
        totalDistance=0;
        avgElevation=0.0;
        elevationDistance = new ArrayList<>();
        distanceTimes= new ArrayList<>();
    }

    public float getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(float totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Double getAvgElevation() {
        return avgElevation;
    }

    public void setAvgElevation(Double avgElevation) {
        this.avgElevation = avgElevation;
    }

    public List<Time> getElevationDistance() {
        return elevationDistance;
    }

    public void setElevationDistance(List<Time> elevationDistance) {
        this.elevationDistance = elevationDistance;
    }

    public List<Time> getDistanceTimes() {
        return distanceTimes;
    }

    public void setDistanceTimes(List<Time> distanceTimes) {
        this.distanceTimes = distanceTimes;
    }
}
