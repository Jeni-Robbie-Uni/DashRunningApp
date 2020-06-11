package com.example.dashrunningapp;

class UserDetails {
    String fName;
    String email;
    String password;
    String sName;


        public String getFName() {
            return this.fName;
        }

        public void setFirstName(String firstname) {
            this.fName = firstname;
        }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String pWord) {
        this.sName = pWord;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.sName = email;
    }

    public String getSurname() {
        return this.sName;
    }

    public void setSurname(String surname) {
        this.sName = surname;
    }

}
