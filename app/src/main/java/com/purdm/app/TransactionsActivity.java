package com.purdm.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


import java.util.ArrayList;
import java.util.List;

public class TransactionsActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rv;
    List<TransactionModel> transactions;
    Api api = null;
    TransactionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Recent Transactions");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv = findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        api = new Api(new Settings(this));
        transactions = new ArrayList<>();
        adapter = new TransactionsAdapter(transactions);
        rv.setAdapter(adapter);
        new httpTask().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class httpTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            loadPageData();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TransactionsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.refreshAdapter(transactions);
                }
            });
        }

    }

    public void loadPageData(){
        Ion.with(TransactionsActivity.this)
                .load(api.getAction(Constants.RECENT_TRANSACTIONS_ACTION_TAG))
                .setLogging("MyLogs", Log.DEBUG)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if( e != null ){
                            Log.d("error", e.getMessage());
                            Snackbar.make(rv, "An error occurred", Snackbar.LENGTH_LONG)
                                    .setAction("Retry", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            new httpTask().execute();
                                        }
                                    }).show();
                        }else{
                            Log.d("results", result.toString());
                            JsonResponse res = new JsonResponse(result);
                            if(res.isGood()){
                                loadTransactions(res.getData().getAsJsonArray("transactions"));
                            }else{
                                Snackbar.make(rv, res.getMessage(), Snackbar.LENGTH_LONG)
                                        .setAction("Retry", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                new httpTask().execute();
                                            }
                                        }).show();
                            }
                        }
                    }
                });
    }

    public void loadTransactions(JsonArray data) {
        for(int i=0; i<= data.size()-1; i++){
            JsonObject obj = data.get(i).getAsJsonObject();
            TransactionModel model = new TransactionModel(obj);
            transactions.add(model);
            Log.d("transaction", model.getCategory());
        }
        adapter.refreshAdapter(transactions);
    }
}
