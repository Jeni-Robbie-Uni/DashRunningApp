package com.example.dashrunningapp;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class UserDetailValidation {


    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    public static Boolean IsEmailValid(EditText email) {
        String emailStr = email.getText().toString();
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);

        return matcher.find();


    }
    public static Boolean IsFieldEmpty(String x){
        return x.length() == 0;
    }

    public static Boolean IsPasswordValid(EditText passField, final int maxLength, final int minLength) {
//Change to regex?
        String password = passField.getText().toString();
        int upCount = 0;
        int loCount = 0;
        int specialCharCount = 0;
        int digit = 0;

        if (password.length() > maxLength || password.length() < minLength) {
            return false;
        } else {

            for (int i = 0; i < password.length(); i++) {
                char c = password.charAt(i);
                if (Character.isUpperCase(c)) {
                    upCount++;
                }
                if (Character.isLowerCase(c)) {
                    loCount++;
                }
                if (Character.isDigit(c)) {
                    digit++;
                }
                if (c >= 33 && c <= 46 || c == 64) {
                    specialCharCount++;
                }
            }
            if (specialCharCount >= 1 && loCount >= 1 && upCount >= 1 && digit >= 1) {
                return true;
            } else {
                return false;
            }
        }
    }
}

