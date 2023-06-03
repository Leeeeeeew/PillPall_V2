package com.example.pillpall;

//model class is used to set and get the data from database

public class Model {
    String title, dosage, date, time;

    public Model() {
    }

    public Model(String title, String dosage, String date, String time) {
        this.title = title;
        this.dosage = dosage;
        this.date = date;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
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
}
