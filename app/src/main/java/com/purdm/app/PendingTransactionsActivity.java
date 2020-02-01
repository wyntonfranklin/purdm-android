package com.purdm.app;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class PendingTransactionsActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rv;
    List<TransactionModel> transactions;
    Api api = null;
    TransactionsAdapter adapter;
    ProgressDialog progress;
    DatabaseConfig db;
    Boolean freshRecords = false;
    SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Pending Transactions");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv = findViewById(R.id.rv);
        db = new DatabaseConfig(this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(),
                llm.getOrientation());
        rv.addItemDecoration(dividerItemDecoration);
        api = new Api(new Settings(this));
        transactions = new ArrayList<>();
        adapter = new TransactionsAdapter(transactions);
        rv.setAdapter(adapter);
        progress = new ProgressDialog(this);
        progress.setMessage("Loading");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                freshRecords = true;
                new savePendingTransactions().execute();

            }
        });
        new httpTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transactions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class httpTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            transactions.clear();
            swipeContainer.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... strings) {
            Cursor results = db.getPendingTransactions();
            loadPageFromDb(results);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            PendingTransactionsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(false);
                }
            });
        }

    }


    public void loadPageFromDb(final Cursor data){
        transactions.clear();
        PendingTransactionsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(data.moveToNext()){
                    do {
                        TransactionModel model =  new TransactionModel(data);
                        transactions.add(model);
                    }while (data.moveToNext());
                }
                adapter.refreshAdapter(transactions);

            }
        });
    }


    public void uploadTransaction(final TransactionModel model){
        Ion.with(this)
                .load(api.createTransactionUrl())
                .setLogging("MyLogs", Log.DEBUG)
                .setBodyParameter("transType", model.getOriginalTransType())
                .setBodyParameter("account", model.getAccountName())
                .setBodyParameter("category", model.getCategory())
                .setBodyParameter("transDate",model.getTransDate())
                .setBodyParameter("description", model.getDescription())
                .setBodyParameter("amount", model.getAmount())
                .setBodyParameter("frequency", model.getFrequency())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if( e != null ){

                        }else{
                            Log.d("results", result.toString());
                            JsonResponse jr = new JsonResponse(result);
                            Log.d("results", jr.getStatus());
                            if(jr.isGood()){
                                db.deletePendingTransaction(model.getId());
                                transactions.remove(model);
                                adapter.refreshAdapter(transactions);
                            }else{

                            }
                        }
                    }
                });
    }

    class savePendingTransactions extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeContainer.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... strings) {
            for (int i=0; i<= transactions.size() -1; i++){
                uploadTransaction(transactions.get(i));
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            swipeContainer.setRefreshing(false);
            PendingTransactionsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }

    }
}
