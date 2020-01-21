package com.purdm.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class InsightsModel {

    public String label;
    public String color;
    public String percentage;
    public String amount;
    public String type="view";

    public InsightsModel(){}

    public InsightsModel(String type){
        this.type = type;
    }

    public InsightsModel(String label, String color, String percentage){
        this.label = label;
        this.color = color;
        this.percentage = percentage;
    }

    public InsightsModel(Cursor data) {
        try{
            this.label = data.getString(1);
            this.percentage = data.getString(2);
            this.amount = data.getString(3);
            this.color = data.getString(4);
        }catch (Exception e ){

        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPercentage() {
        return percentage;
    }

    public Float getFloatPercentage(){
        String ft = String.format ("%,.2f", Float.parseFloat(this.percentage));
        return Float.parseFloat(ft);
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getAmount() {
        String money = this.amount.replaceAll(",","");
        return String.format ("%,.2f", Float.parseFloat(money));
    }

    public Float getFloatAmount(){
        String money = this.amount.replaceAll(",","");
        Log.d("money", money);
        String ft = String.format ("%.2f", Float.parseFloat(money));
        return Float.parseFloat(ft);
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ContentValues getValues(){
        ContentValues values = new ContentValues();
        values.put("label", this.getLabel());
        values.put("percentage", this.getPercentage());
        values.put("amount", this.getAmount());
        values.put("color",this.getColor());
        return values;
    }
}
