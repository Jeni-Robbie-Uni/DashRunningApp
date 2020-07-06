package com.example.dashrunningapp;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class UserDetailValidation {


    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!#%*?&])[A-Za-z\\d$@$!#%*?&]{5,14}");


    public static Boolean IsEmailValid(EditText email) {
        String emailStr = email.getText().toString();
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);

        return matcher.find();


    }
    public static Boolean IsFieldEmpty(String x){
        return x.length() == 0;
    }

    public static Boolean IsPasswordValid(EditText passField, final int maxLength, final int minLength) {
        String password = passField.getText().toString();
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(password);
        return matcher.find();


    }
}

