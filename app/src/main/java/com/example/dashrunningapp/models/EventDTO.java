package com.example.dashrunningapp.models;


//Container for event value
public class EventDTO
{
    private  String name;
    private String url;
    private String location;
    private String city;
    private String date;

    public EventDTO(String name, String url, String location, String city, String date){
        this.name = name;
        this.url = url;
        this.location = location;
        this.city = city;
        this.date = date;


    }

    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }
    public String getCity() {
        return city;
    }
    public String getDate() {
        return date;
    }

}