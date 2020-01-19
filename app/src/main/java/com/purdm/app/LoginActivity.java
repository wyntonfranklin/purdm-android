package com.purdm.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;

import java.net.InetAddress;

public class LoginActivity extends AppCompatActivity {

    Settings settings = null;
    Api api = null;
    Button login;
    EditText domain, email, password;
    ProgressDialog progress;
    DatabaseConfig db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        settings = new Settings(this);
        domain = findViewById(R.id.domain);
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        progress = new ProgressDialog(this);
        progress.setMessage("Loading");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        db = new DatabaseConfig(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDomain();
            }
        });
        if(settings.isUserLoggedIn()){
            goToDashboard();
        }
    }


    public void getDomain(){
        String userDomain = domain.getText().toString();
        if(domain.getText().toString().isEmpty()){
            domain.setError("Please enter domain");
        }else if(email.getText().toString().isEmpty()){
            email.setError("Please enter email");
        }else if(password.getText().toString().isEmpty()){
            password.setError("Please enter password");
        }else{
            settings.insertAsString("domain",userDomain);
            new httpTask().execute();
        }
    }



    public void loginUser(){
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        settings.insertAsString("email", userEmail);
        api = new Api(settings);
        api.setRequest("post");
        Ion.with(LoginActivity.this)
                .load(api.getAction(Constants.USER_CREDENTIALS_ACTION_TAG))
                .setLogging("MyLogs", Log.DEBUG)
                .setBodyParameter("email", userEmail)
                .setBodyParameter("password", userPassword)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if( e != null ){
                            Snackbar.make(email, "An error occurred", Snackbar.LENGTH_LONG)
                                    .setAction("Retry", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            loginUser();
                                        }
                                    }).show();
                        }else{
                            Log.d("results", result.toString());
                            JsonResponse jr = new JsonResponse(result);
                            Log.d("results", jr.getStatus());
                            if(jr.isGood()){
                                if(jr.hasData()){
                                    Log.d("data", jr.getDataKeyAsString("apiKey"));
                                    settings.insertAsString(Constants.SETTINGS_API_TAG,
                                            jr.getDataKeyAsString(Constants.SERVER_API_TAG));
                                    updateUserPreferences(jr);
                                }
                            }else{
                                Snackbar.make(email, jr.getMessage(), Snackbar.LENGTH_LONG)
                                        .setAction("Retry", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                loginUser();
                                            }
                                        }).show();
                            }
                        }
                    }
                });
    }

    public void updateUserPreferences(JsonResponse resp){
        db.clearAccounts(); db.clearCategories(); db.clearAllInsights();
        db.clearAllRecentTransactions(); // referesh all data
        JsonArray cats = resp.getJsonArrayFromData("categories");
        JsonArray accounts = resp.getJsonArrayFromData("accounts");
        Log.d("categories", cats.toString());
        Log.d("accounts", accounts.toString());
        for(int i=0; i<= cats.size()-1; i++){
            db.addCategories(cats.get(i).getAsString());
            Log.d("add category", cats.get(i).getAsString());
        }

        for(int i=0; i<= accounts.size()-1; i++){
            JsonObject obj = accounts.get(i).getAsJsonObject();
            db.addAccount(obj.get("name").getAsString(), obj.get("id").getAsInt());
            Log.d("add account", obj.toString());
        }

        goToDashboard();


    }

    public void goToDashboard(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    class httpTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            loginUser();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            LoginActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress.dismiss();
                }
            });
        }

    }


}
