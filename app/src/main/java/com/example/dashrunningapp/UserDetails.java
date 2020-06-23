package com.example.dashrunningapp;
import java.util.regex.*;


class UserDetails {
    String firstname;
    String email;
    String password;
    String surname;




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




}
