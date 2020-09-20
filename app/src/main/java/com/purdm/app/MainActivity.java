package com.purdm.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView exView, inView, savingsView, networthView;
    Api api;
    public static final String MyPREFERENCES = "MyPrefs" ;
    Settings settings;
    FloatingActionButton fab;
    ProgressDialog progress;
    SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = new Settings(this);
        api = new Api(settings);
        exView = findViewById(R.id.expense);
        inView = findViewById(R.id.income);
        savingsView = findViewById(R.id.savings);
        networthView = findViewById(R.id.networth);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Purdm - Your expense manager");
        setSupportActionBar(toolbar);
        progress = new ProgressDialog(this);
        progress.setMessage("Loading");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateTransactionActivity.class);
                startActivity(intent);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new httpTask().execute();

            }
        });
        if(settings.isDashboardSaved() == true){
            loadDashboardFromSettings();
        }else{
            new httpTask().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id== R.id.action_logout){
            settings.logoutUser();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        if(id == R.id.action_recent_transactions){
            Intent intent = new Intent(this, TransactionsActivity.class);
            startActivity(intent);
        }

        if(id == R.id.action_pending_transactions){
            Intent intent = new Intent(this, PendingTransactionsActivity.class);
            startActivity(intent);
        }
        if(id == R.id.action_insights){
            Intent intent = new Intent(this, InsightsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadPageData(){
        Ion.with(MainActivity.this)
                .load(api.getAction(Constants.INCOME_EXPENSES_ACTION_TAG))
                .setLogging("MyLogs", Log.DEBUG)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if( e != null ){
                           // Log.d("error", e.getMessage());
                            Snackbar.make(fab, "An error occurred", Snackbar.LENGTH_LONG)
                                    .setAction("Retry", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            new httpTask().execute();
                                        }
                                    }).show();
                        }else{
                           JsonResponse res = new JsonResponse(result);
                           if(res.isGood()){
                               setViews(res.getData());
                           }else{
                               Snackbar.make(fab, res.getMessage(), Snackbar.LENGTH_LONG)
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

    public void loadDashboardFromSettings(){
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DashBoardModel model = settings.getDashboardModel();
                try{
                    inView.setText(model.getIncome());
                    exView.setText(model.getExpenses());
                    savingsView.setText(model.getSavings());
                    networthView.setText(model.getNetworth());

                }catch (Exception e){
                   // Log.d("error", e.getMessage());
                }
            }
        });
    }


    public void setViews(JsonObject data){
        DashBoardModel model = new DashBoardModel(data);
        settings.saveDashboardModel(model);
        try{
            inView.setText(model.getIncome());
            exView.setText(model.getExpenses());
            savingsView.setText(model.getSavings());
            networthView.setText(model.getNetworth());

        }catch (Exception e){
            //Log.d("error", e.getMessage());
        }
    }


    class httpTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeContainer.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... strings) {
            loadPageData();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(false);
                }
            });
        }

    }
}
