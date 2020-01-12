package com.purdm.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class LoginActivity extends AppCompatActivity {

    Settings settings = null;
    Api api = null;
    Button login;
    EditText domain, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        settings = new Settings(this);
        domain = findViewById(R.id.domain);
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
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
        settings.insertAsString("domain",userDomain);
        loginUser();
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
                            Log.d("error", e.getMessage());
                        }else{
                            Log.d("results", result.toString());
                            JsonResponse jr = new JsonResponse(result);
                            Log.d("results", jr.getStatus());
                            if(jr.hasData()){
                                Log.d("data", jr.getDataKeyAsString("apiKey"));
                                settings.insertAsString(Constants.SETTINGS_API_TAG,
                                        jr.getDataKeyAsString(Constants.SERVER_API_TAG));
                                goToDashboard();
                            }
                        }
                    }
                });
    }

    public void goToDashboard(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    class httpTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

                }
            });
        }

    }


}
