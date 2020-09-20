package com.purdm.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;

public class InsightsActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rv;
    List<InsightsModel> insights;
    Api api = null;
    InsightsAdapter adapter;
    RecyclerViewHeader header;
    PieChart mPieChart;
    ProgressDialog progress;
    DatabaseConfig db;
    Boolean freshRecords = false;
    SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insights);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new DatabaseConfig(this);
        api = new Api(new Settings(this));
        rv = findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(),
                llm.getOrientation());
        rv.addItemDecoration(dividerItemDecoration);
        insights = new ArrayList<>();
        adapter = new InsightsAdapter(insights);
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
                new httpTask().execute();

            }
        });
        new httpTask().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_insights, menu);
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
            swipeContainer.setRefreshing(true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            Cursor results = db.getInsights();
            if(results.getCount() > 0 && freshRecords==false){
                loadPageDataFromDb(results);
            }else{
                loadPageData();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            InsightsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(false);
                }
            });
        }

    }

    public void loadPageDataFromDb(final Cursor data){
        insights.clear();
        insights.add(new InsightsModel("header")); // empty for header
        InsightsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(data.moveToNext()){
                    do {
                        InsightsModel model =  new InsightsModel(data);
                        insights.add(model);
                    }while (data.moveToNext());
                }
                adapter.refreshAdapter(insights);

            }
        });
    }

    public void loadPageData(){
        insights.clear();
        Ion.with(this)
                .load(api.getAction(Constants.EXPENSES_BY_CATEGORY_ACTION_TAG))
                .setLogging("MyLogs", Log.DEBUG)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if( e != null ){
                            //Log.d("error", e.getMessage());
                            Snackbar.make(rv, "An error occurred", Snackbar.LENGTH_LONG)
                                    .setAction("Retry", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            new httpTask().execute();
                                        }
                                    }).show();
                        }else{
                            JsonResponse res = new JsonResponse(result);
                            if(res.isGood()){
                              loadInsights(res);
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

    public void loadInsights(JsonResponse jr){
        db.clearAllInsights();
        JsonArray labels = jr.getJsonArrayFromData("labels");
        JsonArray colors = jr.getJsonArrayFromData("colors");
        JsonArray percentages = jr.getJsonArrayFromData("percentages");
        JsonArray amounts = jr.getJsonArrayFromData("dataset");
        insights.add(new InsightsModel("header")); // empty for header
        for(int i=0; i<= labels.size()-1; i++){
            InsightsModel model = new InsightsModel(labels.get(i).getAsString(),
                    colors.get(i).getAsString(), percentages.get(i).getAsString());
            model.setAmount(amounts.get(i).getAsString());
           Log.d("label", model.getLabel());
            insights.add(model);
            db.addInsights(model);
        }
        adapter.refreshAdapter(insights);


    }


}
