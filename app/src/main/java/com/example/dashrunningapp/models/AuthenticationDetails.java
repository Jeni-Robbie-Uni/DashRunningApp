package com.example.dashrunningapp.models;

//Singleton class use to persist one authentication  token for duration of application
public class AuthenticationDetails {

    private static String token;
    private static AuthenticationDetails self;
        //private constructor
        private AuthenticationDetails() {
            token=null;

        }
        //public getter to create or return AuthenticationDetails object
        public static AuthenticationDetails getInstance() {
            if (self == null) { //check if AuthenticationDetails object exist
                self = new AuthenticationDetails(); //if not create one
            }
            return self;    //else return the only AuthenticationDetails object that exists
        }

    public static String getToken() {
        return token;
    }
    public static void setToken(String newToken) {
        token= newToken;
    }

}

