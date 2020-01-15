package com.purdm.app;

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
        return amount;
    }

    public Float getFloatAmount(){
        String ft = String.format ("%,.2f", Float.parseFloat(this.amount));
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
}
