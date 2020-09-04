package com.example.dashrunningapp.Misc;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


//formats objects and strings to json strings and objects
public class JsonFormater {
    //Converts objects to json strings
    static public <T> String convertToJString(T x) {
        String jsonString="";
        return jsonString= new Gson().toJson(x);
    }
    //converts json strings to json objects
    static public JSONObject convertToJsonObj(String x) throws JSONException {
      return new JSONObject(x);
    }
}



