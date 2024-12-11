package com.example.campusexpensemanager.Model;


public class Expenses {
    public String id;
    public String name;
    public String amount;
    public String note;
    public String date;

    public Expenses(String amount) {
    }

    public Expenses(String id, String amount, String name, String note, String date) {
        this.id = id;
        this.amount = amount;
        this.name = name;
        this.note = note;
        this.date = date;
    }



    public void setId(String id) {
        this.id = id;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDate(String date) {
        date = date;
    }

    public String getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }
}
