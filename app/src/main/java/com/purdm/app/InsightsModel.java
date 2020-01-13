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

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getAmount() {
        return amount;
    }

    public Float getFloatAmount(){
        return Float.parseFloat(this.getAmount());
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
