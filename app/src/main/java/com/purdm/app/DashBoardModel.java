package com.purdm.app;

import android.util.Log;

import com.google.gson.JsonObject;

public class DashBoardModel {

    private String income;
    private String expenses;
    private String savings;
    private String networth;

    public DashBoardModel(){}

    protected DashBoardModel(JsonObject in) {
        try{
            this.income = in.get("income").getAsString();
            this.expenses= in.get("expenses").getAsString();
            this.networth = in.get("worth").getAsString();
            this.savings = in.get("savings").getAsString();
        }catch (Exception e){
            Log.d("error", e.getMessage());
        }
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getExpenses() {
        return expenses;
    }

    public void setExpenses(String expenses) {
        this.expenses = expenses;
    }

    public String getSavings() {
        return savings;
    }

    public void setSavings(String savings) {
        this.savings = savings;
    }

    public String getNetworth() {
        return networth;
    }

    public void setNetworth(String networth) {
        this.networth = networth;
    }
}
