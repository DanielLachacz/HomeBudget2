package com.example.daniellachacz.homebudget;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Income {

    Long incomeDateLong = System.currentTimeMillis();
    String incomeDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date(incomeDateLong));
    private String incomeDescription;
    private Double incomeValue;

    static Calendar cal = Calendar.getInstance();
    int incomeMonth = cal.get(Calendar.MONTH);
    int incomeWeek = cal.get(Calendar.WEEK_OF_YEAR);
    int incomeDay = cal.get(Calendar.DAY_OF_MONTH);

    public Income() {

    }

    public Income(String incomeDescription, Double incomeValue) {
        this.incomeDescription = incomeDescription;
        this.incomeValue = incomeValue;

    }

    public String getIncomeDescription() {
        return incomeDescription;
    }

    public void setIncomeDescription(String incomeDescription) {
        this.incomeDescription = incomeDescription;
    }

    public String getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(String incomeDate) {
        this.incomeDate = incomeDate;
    }

    public Double getIncomeValue() {
        return incomeValue;
    }

    public void setIncomeValue(Double incomeValue) {
        this.incomeValue = incomeValue;
    }


}
