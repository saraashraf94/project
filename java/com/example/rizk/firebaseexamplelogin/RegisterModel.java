package com.example.rizk.firebaseexamplelogin;

/**
 * Created by rizk on 31/03/18.
 */

public class RegisterModel {

    String email , password,fName,lName;
    public RegisterModel(String email , String fName,String lName , String password){

        this.email=email;
        this.fName=fName;
        this.lName=lName;
        this.password=password;


    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }
}
