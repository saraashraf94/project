package com.example.rizk.firebaseexamplelogin;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rizk on 23/03/18.
 */

public class SaveData {
    public String id;
    public String name;
    public String from;
    public String to;
    public String date;
    public String time;
    public String notes;
    public String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public SaveData() {
    }

    public SaveData(String name,String from, String to, String date, String time, String notes,String status) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.time = time;
        this.notes = notes;
        this.name=name;
        this.status=status;

    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name",name);
        result.put("from:", from);
        result.put("to", to);
        result.put("date", date);
        result.put("time", time);
        result.put("notes", notes);
        result.put("id",id);
        result.put("status",status);


        return result;
    }
}