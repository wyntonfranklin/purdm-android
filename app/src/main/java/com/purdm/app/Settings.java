package com.purdm.app;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences _settings;
    public String apikey = "";

    public Settings(){

    }

    public Settings(Context context){
        this._settings = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public void insertAsString(String key, String value){
        SharedPreferences.Editor editor = this.getSettings().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void saveDashboardModel(DashBoardModel model){
        SharedPreferences.Editor editor = this.getSettings().edit();
        editor.putString("income", model.getIncome());
        editor.putString("expenses", model.getExpenses());
        editor.putString("savings", model.getSavings());
        editor.putString("networth", model.getNetworth());
        editor.putBoolean("dashSaved", true);
        editor.commit();
    }

    public DashBoardModel getDashboardModel(){
        DashBoardModel model = new DashBoardModel();
        model.setIncome(this.getString("income",""));
        model.setExpenses(this.getString("expenses",""));
        model.setSavings(this.getString("savings",""));
        model.setNetworth(this.getString("networth",""));
        return model;
    }

    public Boolean isDashboardSaved(){
        return this.getSettings().getBoolean("dashSaved", false);
    }


    public SharedPreferences getSettings(){
        return this._settings;
    }

    public String getString(String key, String default_value){
        return this.getSettings().getString(key,default_value);
    }

    public void logoutUser(){
        SharedPreferences.Editor editor = this.getSettings().edit();
        editor.remove(Constants.SETTINGS_API_TAG);
        editor.remove(Constants.SETTING_EMAIL_TAG);
        editor.putBoolean("dashSaved", false);
        editor.commit();
    }

    public boolean isUserLoggedIn(){
        return this.getSettings().contains(Constants.SETTINGS_API_TAG);
    }
}
