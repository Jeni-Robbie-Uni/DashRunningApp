package com.example.dashrunningapp.models;


//Container for user details
public class UserDetails {
    private String firstname;
    private String email;
    private String password;
    private String surname;



//this should
    public UserDetails(String userFirsname, String userSurnmae, String userEmail, String userPassword) {
        this.firstname=userFirsname;
        this.surname=userSurnmae;
        this.email=userEmail;
        this.password=userPassword;
    }

    public UserDetails() {

    }
    public UserDetails( String userEmail, String userPassword) {
        email=userEmail;
        password=userPassword;
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

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }






}
