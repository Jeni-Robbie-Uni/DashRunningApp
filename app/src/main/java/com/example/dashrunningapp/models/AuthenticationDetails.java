package com.example.dashrunningapp.models;

public class AuthenticationDetails {

    private static String token;
    private static AuthenticationDetails self;

        private AuthenticationDetails() {
            token=null;

        }

        public static AuthenticationDetails getInstance() {
            if (self == null) {
                self = new AuthenticationDetails();
            }
            return self;
        }

    public static String getToken() {
        return token;
    }
    public static void setToken(String newToken) {
        token= newToken;
    }

}

