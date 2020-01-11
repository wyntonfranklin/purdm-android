package com.purdm.app;

import android.content.Context;
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        new httpTask().execute();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadPageData(){
        Ion.with(MainActivity.this)
                .load(api.getAction("ie"))
                .setLogging("MyLogs", Log.DEBUG)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if( e != null ){
                            Log.d("error", e.getMessage());
                        }else{
                           Log.d("results", result.toString());
                            //JsonObject data = result.get("data");
                            JsonObject response = result.getAsJsonObject("response");
                            JsonObject data = response.getAsJsonObject("data");
                            Log.d("income", data.get("income").toString());
                            setViews(data);
                        }
                    }
                });
    }


    public void setViews(JsonObject data){
        DashBoardModel model = new DashBoardModel(data);
        try{
            inView.setText(model.getIncome());
            exView.setText(model.getExpenses());
            savingsView.setText(model.getSavings());
            networthView.setText(model.getNetworth());

        }catch (Exception e){
            Log.d("error", e.getMessage());
        }
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
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }

    }
}