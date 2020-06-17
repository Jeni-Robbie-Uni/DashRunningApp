package com.example.dashrunningapp;
import java.util.regex.*;


class UserDetails {
    String firstname;
    String email;
    String password;
    String surname;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);



    public UserDetails(String userFirsname, String userSurnmae, String userEmail, String userPassword) {
        firstname=userFirsname;
        surname=userSurnmae;
        email=userEmail;
        password=userPassword;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFName() {
            return this.firstname;
        }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String pWord) {
        this.password = pWord;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }



    public static Boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();


    }
    public Boolean validate() {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(this.email);
        return matcher.find();
    }


}
