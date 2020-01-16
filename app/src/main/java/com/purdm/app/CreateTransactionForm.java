package com.purdm.app;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CreateTransactionForm {

    private Context _context;
    private TextView category;
    private TextView description;
    private TextView amount;
    private TextView memo;

    public CreateTransactionForm(Activity m){

        this._context = m;
        this.category = m.findViewById(R.id.category);
        this.description = m.findViewById(R.id.transDescription);
        this.amount = m.findViewById(R.id.amount);
        this.memo = m.findViewById(R.id.memo);
    }


    public String getCategory(){
        return  this.category.getText().toString();
    }

    public String getDescription(){
        return this.description.getText().toString();
    }

    public String getMemo(){
        return this.memo.getText().toString();
    }

    public String getAmount(){
        return this.amount.getText().toString();
    }

    public String getAccount(){
        return "1";
    }

    public String getFrequency(){
        return "";
    }

    public String getDate(){
        return "";
    }

    public String getType(){
        return "expense";
    }

    public Boolean validate(){
        return false;
    }

    public void clearForm(){
        this.category.setText("");
        this.description.setText("");
        this.amount.setText("");
        this.memo.setText("");
    }

    @Override
    public String toString(){
        return getCategory() + ":"
            + getDescription() + ":"
            + getMemo();
    }

}
