package com.example.dashrunningapp.Misc;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;




    public class JsonFormater {

        static public <T> String convertToJString(T x) {
            String jsonString="";
            return jsonString= new Gson().toJson(x);
        }
        static public JSONObject convertToJsonObj(String x) throws JSONException {
          return new JSONObject(x);
        }


    }



