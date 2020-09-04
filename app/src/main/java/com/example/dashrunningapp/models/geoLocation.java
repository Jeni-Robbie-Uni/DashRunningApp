package com.example.dashrunningapp.models;
//Container for temp location value
public class geoLocation {
    private Double longitude;
    private Double latitude;
    private Double altitude;
//getters and setters
    public Double getLongitude() { return this.longitude; }

    public Double getLatitude() { return this.latitude; }
    public Double getAltitude() { return this.altitude; }

    public void setLatitude(Double latitude) {
        this.latitude= latitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude=longitude;
    }
    public void setAltitude(Double altitude) {
        this.altitude=altitude;
    }

}
