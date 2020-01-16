package com.purdm.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseConfig extends SQLiteAssetHelper {

    private static String DB_Name = "purdm";
    private static final int DB_Version = 1;



    public DatabaseConfig(Context context) {
        super(context,DB_Name,null,DB_Version);

    }

    public void addTransaction(TransactionModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = model.getValues();
        db.insert(Constants.DB_LOCAL_TRANSACTIONS, null, values);
        db.close();
    }

    public void addRecentTransactions(){

    }

    public void addInsights(){

    }

    public void addCategory(){

    }

    public void addAccount(){

    }

    public void clearRecentTransactions(){

    }

    public void clearInsights(){

    }



}

