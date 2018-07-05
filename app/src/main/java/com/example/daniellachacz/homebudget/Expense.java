package com.example.daniellachacz.homebudget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Expense {

    Long dateLong = System.currentTimeMillis();
    String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(dateLong));
    private String description;
    private Double value;

    static Calendar cal = Calendar.getInstance();
    int month = cal.get(Calendar.MONTH);
    int week = cal.get(Calendar.WEEK_OF_YEAR);
    int day = cal.get(Calendar.DAY_OF_MONTH);

    public Expense() {

    }

    public Expense(String description, Double value) {

        this.description = description;
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
