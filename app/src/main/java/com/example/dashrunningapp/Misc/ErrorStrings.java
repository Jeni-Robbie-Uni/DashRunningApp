package com.example.dashrunningapp.Misc;

//Class holds error response strings
public class ErrorStrings {
    public static String getInvalidEmailError() {
        return "\"Invalid email. Needs to be an email  e.g. example@gmail.com\"";
    }
    public static String getEmptyFieldError() {
        return "\"Field cannot be empty\"";
    }
    public static String getLoctaionNotFoundError() {
        return "\"Cannot determine location\"";
    }
    public static String getUnrecoverableError() {
        return "\"Error. Please contact developer if error persists\"";
    }
    public static String getTermsNotAgreedError() {
        return "\"Users must Agree to Terms and conditions before proceeding\"\"";
    }

    public static String getPasswordInvalidError() {
        return "\"  \"Invalid password, password must be between 5 and 14 characters, contain a number and a special chracter e.g. !&%\"\"";
    }
    public static String getLoginFailureError() {
        return "\"Unable to log in. Please enter correct credentials \"\"";
    }
    public static String getServerError() {
        return "\"Server Error. Please try again later or contact support \"\"";
    }
    public static String getNetworkError() {
        return "\"Network Error. Please try again later or contact support \"\"";
    }
    public static String getTimeoutError() {
        return "\"Connect failed or timed out. Please try again later or contact support \"\"";
    }
    public static String getAccountTakenError() {
        return "\"Account already registered \"\"";
    }

}
