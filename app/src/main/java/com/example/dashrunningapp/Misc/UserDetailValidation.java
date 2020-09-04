package com.example.dashrunningapp.Misc;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


//class contains static functions to validate properties of User Details class
public class UserDetailValidation {

    //define email regular expression
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    //Define password  regular expression
    public static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!#%*?&])[A-Za-z\\d$@$!#%*?&]{5,14}");

    //Validate email in edit text box with regular expression
    public static Boolean IsEmailValid(EditText email) {
        String emailStr = email.getText().toString();
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr); //Matcher object compares strings with pattern

        return matcher.find(); //returns boolean, goes through each character to find match


    }
    //Check that edit text field is empty
    public static Boolean IsFieldEmpty(String x){
        return x.length() == 0;
    }

    //Validate password in edit text box with regular expression
    public static Boolean IsPasswordValid(EditText passField) {
        String password = passField.getText().toString();
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(password);   //Matcher object compares strings with pattern
        return matcher.find();      //returns boolean, goes through each character to find match


    }
}

