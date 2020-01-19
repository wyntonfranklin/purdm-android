package com.purdm.app;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CreateTransactionForm {

    private Context _context;
    private Spinner category;
    private Spinner accounts;
    private Spinner frequency;
    private Spinner transType;
    private TextView transDate;
    private TextView description;
    private TextView amount;
    private TextView memo;
    public DatabaseConfig db;
    public List<String> accountsList;
    public List<String> accountsId;


    public CreateTransactionForm(Activity m){

        this._context = m;
        this.accountsList = new ArrayList<>();
        this.accountsId = new ArrayList<>();
        this.category = m.findViewById(R.id.category);
        this.description = m.findViewById(R.id.transDescription);
        this.amount = m.findViewById(R.id.amount);
        this.memo = m.findViewById(R.id.memo);
        this.accounts = m.findViewById(R.id.accounts);
        this.frequency = m.findViewById(R.id.frequency);
        this.transDate = m.findViewById(R.id.transDate);
        this.transType = m.findViewById(R.id.transType);
        this.db = new DatabaseConfig(this._context);
        loadCategoriesFromDb();
        loadAccountsFromDb();
    }


    public String getCategory(){
        return this.category.getSelectedItem().toString();
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
        return this.getAccountId();
    }

    public String getFrequency(){
        return this.frequency.getSelectedItem().toString();
    }

    public String getDate(){
        return this.transDate.getText().toString();
    }

    public String getType(){
        return this.transType.getSelectedItem().toString();
    }

    public Boolean validate(){
        return false;
    }

    public void clearForm(){
        this.description.setText("");
        this.amount.setText("");
        this.memo.setText("");
    }

    public String getAccountId(){
        int pos = this.accounts.getSelectedItemPosition();
        return this.accountsId.get(pos);
    }

    public void loadCategoriesFromDb(){
        List<String> categories = this.db.getCategoriesList();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this._context, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.category.setAdapter(dataAdapter);
    }

    public void loadAccountsFromDb(){
        Cursor accounts = this.db.getAccounts();
        if(accounts.moveToNext()){
            do {
                this.accountsList.add(accounts.getString(1));
                this.accountsId.add(accounts.getString(0));
            }while (accounts.moveToNext());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this._context, android.R.layout.simple_spinner_item, this.accountsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.accounts.setAdapter(dataAdapter);
    }

    @Override
    public String toString(){
        return getCategory() + ":"
            + getDescription() + ":"
            + getMemo() + ":"
                + getAccountId();
    }

}
