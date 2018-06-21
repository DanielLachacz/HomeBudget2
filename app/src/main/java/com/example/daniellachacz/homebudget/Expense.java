package com.example.daniellachacz.homebudget;

public class Expense {

    String description, date;
    Double value;


    public Expense() {

    }

    public Expense( String description, String date, Double value) {

        this.description = description;
        this.date = date;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
