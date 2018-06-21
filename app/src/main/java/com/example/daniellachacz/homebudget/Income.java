package com.example.daniellachacz.homebudget;

public class Income {

    public String incomeDescription, incomeDate;
    public Double incomeValue;

    public Income() {

    }

    public Income(String incomeDescription, String incomeDate, Double incomeValue) {
        this.incomeDescription = incomeDescription;
        this.incomeDate = incomeDate;
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
