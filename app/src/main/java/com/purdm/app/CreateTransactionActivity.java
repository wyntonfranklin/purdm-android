package com.purdm.app;

import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveForm(){
        Ion.with( CreateTransactionActivity.this)
                .load(api.getAction(Constants.USER_CREDENTIALS_ACTION_TAG))
                .setLogging("MyLogs", Log.DEBUG)
                .setBodyParameter("transType", "")
                .setBodyParameter("account", "")
                .setBodyParameter("category", form.getCategory())
                .setBodyParameter("transDate","")
                .setBodyParameter("description", form.getDescription())
                .setBodyParameter("amount", "")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        //pb.setVisibility(View.GONE);
                        if( e != null ){

                        }else{
                            // Log.d("results", result.toString());
                            JsonResponse jr = new JsonResponse(result);
                            Log.d("results", jr.getStatus());
                            if(jr.isGood()){
                                if(jr.hasData()){

                                }
                            }else{

                            }
                        }
                    }
                });
    }

}
