package com.example.dashrunningapp.Misc;

//Holds server address strings to build custom API requests
public class ApiStringConstants {

    public static String getServerAddress() {
        return "http://localhost:5000";
    }
    public static String getLogin() {
        return "/api/Login";
    }
    public static String getUserAddress() {
        return "/api/Users";
    }
    public static String getEventAddress() {
        return "/api/RunningEvents";
    }
    public static String getCharset() {
        return "application/json; charset=utf-8";
    }



}
