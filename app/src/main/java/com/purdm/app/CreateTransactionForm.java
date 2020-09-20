package com.purdm.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.timessquare.CalendarPickerView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private String currentDate;
    public DatabaseConfig db;
    public List<String> accountsList;
    public List<String> accountsId;
    public String[] frequencyValue =  {"","year","month","week","day"};


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
        this.onInit();
    }

    public void onInit(){
        loadCategoriesFromDb();
        loadAccountsFromDb();

        this.transDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              openCalendarView();
            }
        });
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
        return this.frequencyValue[this.frequency.getSelectedItemPosition()];
    }

    public String getDate(){
        return this.transDate.getText().toString();
    }

    public String getType(){
        return this.transType.getSelectedItem().toString();
    }

    public Boolean validate(){

        if(this.getDate().equals("")){
            this.transDate.setError("Date cannot be blank");
            return false;
        }

        if(this.validateTransDate() == false){
            this.transDate.setError("Date is not correct format");
            return false;
        }
        if(this.getDescription().equals("")){
            this.description.setError("Description cannot be blank");
            return false;
        }
        if(this.getAmount().equals("")){
            this.amount.setError("Amount cannot be blank");
            return false;
        }
        return true;
    }

    private Boolean validateTransDate(){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd", Locale.getDefault());
            Date dt = dateFormat.parse(this.getDate());
            this.transDate.setError(null);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void clearForm(){
        this.description.setText("");
        this.amount.setText("");
        this.memo.setText("");
    }

    public TextView getElementDescription(){
        return this.description;
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


    public void saveInDb(){

    }

    public void openCalendarView(){
        View view;
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Calendar prevYear = Calendar.getInstance();
        prevYear.add(Calendar.YEAR,-1);
        LayoutInflater inflater=(LayoutInflater)this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view =inflater.inflate(R.layout.calendar_layout,null);
        final CalendarPickerView calendar = view.findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(prevYear.getTime(), nextYear.getTime())
                  .withSelectedDate(today);
        AlertDialog alertDialog = new AlertDialog.Builder(this._context).create();
        alertDialog.setTitle("Select Day");
        alertDialog.setView(view);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        transDate.setText(getDateTime(calendar.getSelectedDate()));
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public String toString(){
        return getCategory() + ":"
            + getDescription() + ":"
            + getMemo() + ":"
                + getAccountId();
    }

    private String getDateTime(Date date) {
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd", Locale.getDefault());
            return dateFormat.format(date);
        }catch (Exception e){
            return "";
        }
    }

    public void saveTransaction(){
        ContentValues values = new ContentValues();
        values.put("trans_date", this.getDate());
        values.put("amount", this.getAmount());
        values.put("description", this.getDescription());
        values.put("category", this.getCategory());
        values.put("type",this.getType());
        values.put("account", this.getAccountId());
        values.put("memo", this.getMemo());
        db.addPendingTransactions(values);
    }

}
