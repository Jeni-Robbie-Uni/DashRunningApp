package com.example.dashrunningapp;

class UserDetails {
    String firstname;
    String email;
    String password;
    String surname;

    public UserDetails(String userEmail, String userFirsname, String userSurnmae, String userPassword) {
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
