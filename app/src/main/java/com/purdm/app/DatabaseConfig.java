package com.purdm.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseConfig extends SQLiteAssetHelper {

    private static String DB_Name = "purdm";
    private static final int DB_Version = 2;



    public DatabaseConfig(Context context) {
        super(context,DB_Name,null,DB_Version);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TABLE_NAME");
        onCreate(db);
    }

    public void addTransaction(TransactionModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = model.getValues();
        db.insert(Constants.DB_LOCAL_TRANSACTIONS, null, values);
        db.close();
    }

    public void addRecentTransactions(TransactionModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = model.getValues();
        db.insert(Constants.DB_RECENT_TRANSACTIONS, null, values);
        db.close();
    }

    public void addCategories(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        db.insert(Constants.DB_CATEGORIES, null, values);
        db.close();
    }

    public void addAccount(String name, int Id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", Id);
        values.put("name", name);
        db.insert(Constants.DB_ACCOUNTS, null, values);
        db.close();
    }

    public Cursor getRecentTransactions(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] sqlSelect = {"id","trans_date","amount", "description","category","type","memo","account"};
        String sqlTables = Constants.DB_RECENT_TRANSACTIONS;
        qb.setTables(sqlTables);
        Cursor cursor = qb.query(db, sqlSelect,null,null, null, null,null, null);
        return cursor;
    }

    public Cursor getCategories(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] sqlSelect = {"name"};
        String sqlTables = Constants.DB_CATEGORIES;
        qb.setTables(sqlTables);
        Cursor cursor = qb.query(db, sqlSelect,null,null, null, null,null, null);
        return cursor;
    }

    public Cursor getAccounts(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] sqlSelect = {"id","name"};
        String sqlTables = Constants.DB_ACCOUNTS;
        qb.setTables(sqlTables);
        Cursor cursor = qb.query(db, sqlSelect,null,null, null, null,null, null);
        return cursor;
    }

    public List<String> getCategoriesList(){
        List<String> categories = new ArrayList<String>();
        Cursor cats = this.getCategories();
        if(cats.moveToNext()){
            do {
               categories.add(cats.getString(0));
            }while (cats.moveToNext());
        }
        return categories;
    }


    public void clearAllRecentTransactions(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlTables = Constants.DB_RECENT_TRANSACTIONS;
        db.delete(sqlTables, null, null);
        db.close();
    }

    public void addInsights(InsightsModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = model.getValues();
        db.insert(Constants.DB_INSIGHTS, null, values);
        db.close();
    }

    public Cursor getInsights(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] sqlSelect = {"id","label","percentage","amount","color"};
        String sqlTables = Constants.DB_INSIGHTS;
        qb.setTables(sqlTables);
        Cursor cursor = qb.query(db, sqlSelect,null,null, null, null,null, null);
        return cursor;
    }

    public void clearAllInsights(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlTables = Constants.DB_INSIGHTS;
        db.delete(sqlTables, null, null);
        db.close();
    }

    public void clearAccounts(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlTables = Constants.DB_ACCOUNTS;
        db.delete(sqlTables, null, null);
        db.close();
    }

    public void clearCategories(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlTables = Constants.DB_CATEGORIES;
        db.delete(sqlTables, null, null);
        db.close();
    }



}

