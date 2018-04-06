package com.example.rizk.firebaseexamplelogin;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rizk on 26/03/18.
 */

public class CommingFirebase {

    public String stEmail,stFirstname,stLastname,stPassword;

    public CommingFirebase(){

    }
    public CommingFirebase(String stEmail, String stFirstname, String stLastname, String stPassword) {
        this.stEmail = stEmail;
        this.stFirstname = stFirstname;
        this.stLastname = stLastname;
        this.stPassword = stPassword;
        toMap();
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("stEmail:", stEmail);
        result.put("stFirstname", stFirstname);
        result.put("stLastname",stLastname);
        result.put("stPassword",stPassword);


        return result;
    }
}
