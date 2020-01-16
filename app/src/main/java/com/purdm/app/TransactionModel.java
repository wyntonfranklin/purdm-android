package com.purdm.app;

import android.content.ContentValues;

import com.google.gson.JsonObject;

public class TransactionModel {

    public String transDate;
    public String amount;
    public String description;
    public String category;
    public String transType;
    public String accountName;
    public String memo;
    public String frequency;


    public TransactionModel(){

    }

    public TransactionModel(JsonObject data){
        try{
            this.transDate = data.get("transDate").getAsString();
            this.amount = data.get("amount").getAsString();
            this.description = data.get("description").getAsString();
            this.category = data.get("category").getAsString();
            this.transType = data.get("type").getAsString();
            this.accountName = data.get("accountName").getAsString();
            this.memo = data.get("memo").getAsString();
            this.frequency = data.get("frequency").getAsString();
        }catch (Exception e){

        }
    }



    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getHeader(){
        String ucCategory = this.category;
        String money = this.amount;
        return ucCategory + " - " + money;
    }

    public String getTransType(){
        return this.transType.substring(0, 1).toUpperCase()
                + this.transType.substring(1);
    }

    public ContentValues getValues(){
        ContentValues values = new ContentValues();
        return values;
    }
}
