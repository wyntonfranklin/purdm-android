package com.purdm.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class CreateTransactionActivity extends AppCompatActivity {

    CreateTransactionForm form;
    Api api;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progress = new ProgressDialog(this);
        progress.setMessage("Loading");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        api = new Api(new Settings(this));
        form = new CreateTransactionForm(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if(id == R.id.action_save){
            Log.d("form elements", form.toString());
            new httpTask().execute();
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveForm(){
        Ion.with( CreateTransactionActivity.this)
                .load(api.createTransactionUrl())
                .setLogging("MyLogs", Log.DEBUG)
                .setBodyParameter("transType", form.getType())
                .setBodyParameter("account", form.getAccount())
                .setBodyParameter("category", form.getCategory())
                .setBodyParameter("transDate",form.getDate())
                .setBodyParameter("description", form.getDescription())
                .setBodyParameter("amount", form.getAmount())
                .setBodyParameter("frequency",form.getFrequency())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        //pb.setVisibility(View.GONE);
                        if( e != null ){

                        }else{
                             Log.d("results", result.toString());
                            JsonResponse jr = new JsonResponse(result);
                            Log.d("results", jr.getStatus());
                            if(jr.isGood()){
                                form.clearForm();
                                if(jr.hasData()){

                                }
                            }else{

                            }
                        }
                    }
                });
    }

    class httpTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            saveForm();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            CreateTransactionActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress.dismiss();
                }
            });
        }

    }

}
