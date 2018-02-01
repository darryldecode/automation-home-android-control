package com.darrylfernandez.homeautomation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SettingsSaveCallBack {

    protected OkHttpClient httpClient;
    protected String Ip;
    protected String Port;
    protected String Token;
    protected ProgressDialog progressDialog;

    // ui
    ImageView switch1ImgView;
    ImageView switch2ImgView;
    ImageView switch3ImgView;
    ImageView switch4ImgView;
    ImageView switch5ImgView;
    ImageView switch6ImgView;
    ImageView switch7ImgView;
    ImageView switch8ImgView;
    Button checkSwitchStatusButton;

    protected ArrayList<Switch> switches = new ArrayList<Switch>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // create the Http Client
        httpClient = new OkHttpClient();

        // set settings
        _getUpdatedSettings();

        // UI call only once
        switch1ImgView = findViewById(R.id.control1);
        switch2ImgView = findViewById(R.id.control2);
        switch3ImgView = findViewById(R.id.control3);
        switch4ImgView = findViewById(R.id.control4);
        switch5ImgView = findViewById(R.id.control5);
        switch6ImgView = findViewById(R.id.control6);
        switch7ImgView = findViewById(R.id.control7);
        switch8ImgView = findViewById(R.id.control8);
        checkSwitchStatusButton = findViewById(R.id.btnCheckSwitchStatus);

        // get current switch status if possible
        _getSwitchStatus();
    }

    private void _getUpdatedSettings() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // server config
        Ip = preferences.getString("ip", "");
        Port = preferences.getString("port", "");
        Token = preferences.getString("token", "");

        // switch names
        TextView sw1Label = findViewById(R.id.labelSwitch1);
        TextView sw2Label = findViewById(R.id.labelSwitch2);
        TextView sw3Label = findViewById(R.id.labelSwitch3);
        TextView sw4Label = findViewById(R.id.labelSwitch4);
        TextView sw5Label = findViewById(R.id.labelSwitch5);
        TextView sw6Label = findViewById(R.id.labelSwitch6);
        TextView sw7Label = findViewById(R.id.labelSwitch7);
        TextView sw8Label = findViewById(R.id.labelSwitch8);

        sw1Label.setText(preferences.getString("switch1", ""));
        sw2Label.setText(preferences.getString("switch2", ""));
        sw3Label.setText(preferences.getString("switch3", ""));
        sw4Label.setText(preferences.getString("switch4", ""));
        sw5Label.setText(preferences.getString("switch5", ""));
        sw6Label.setText(preferences.getString("switch6", ""));
        sw7Label.setText(preferences.getString("switch7", ""));
        sw8Label.setText(preferences.getString("switch8", ""));
    }

    private void _getSwitchStatus() {

        checkSwitchStatusButton.setEnabled(false);

        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Checking");
        progressDialog.setMessage("Checking server connection, please wait..");
        progressDialog.show();

        if(Ip.equals("") || Port.equals("") || Token.equals("")) {
            Toast.makeText(getApplicationContext(),"Server settings needed. Check Settings.",Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://" + Ip + ":" + Port + "/status";

        Log.i("URL",url);

        try {
            OkHttpHandler okHttpHandler = new OkHttpHandler();
            okHttpHandler.delegate = new AfterCheckSwitchStatusCallback();
            okHttpHandler.execute(url);
        } catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Cannot get switch status at the moment. Check Settings.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

            SettingsActivity.delegate = this;

            Intent SettingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            MainActivity.this.startActivity(SettingsIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void clickGetSwitchStatus(View v) {
        _getUpdatedSettings();
        _getSwitchStatus();
    }

    public void clickSwitch(View v) throws IOException {

        String url = "http://" + Ip + ":" + Port + "?token=" + Token;

        if(switches.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please check switch status first.",Toast.LENGTH_SHORT).show();
            return;
        }

        switch (v.getId()) {

            case R.id.cardSwitch1:
                if(switches.get(0).value.equals("1"))
                {
                    url += "&control=1&value=0";
                    switches.get(0).value = "0";
                    switch1ImgView.setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                    switch1ImgView.setBackground(getResources().getDrawable(R.drawable.grey));
                } else {
                    url += "&control=1&value=1";
                    switches.get(0).value = "1";
                    switch1ImgView.setImageResource(R.drawable.ic_check_black_24dp);
                    switch1ImgView.setBackground(getResources().getDrawable(R.drawable.green));
                }
                break;
            case R.id.cardSwitch2:
                if(switches.get(1).value.equals("1"))
                {
                    url += "&control=2&value=0";
                    switches.get(1).value = "0";
                    switch2ImgView.setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                    switch2ImgView.setBackground(getResources().getDrawable(R.drawable.grey));
                } else {
                    url += "&control=2&value=1";
                    switches.get(1).value = "1";
                    switch2ImgView.setImageResource(R.drawable.ic_check_black_24dp);
                    switch2ImgView.setBackground(getResources().getDrawable(R.drawable.green));
                }
                break;
            case R.id.cardSwitch3:
                if(switches.get(2).value.equals("1"))
                {
                    url += "&control=3&value=0";
                    switches.get(2).value = "0";
                    switch3ImgView.setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                    switch3ImgView.setBackground(getResources().getDrawable(R.drawable.grey));
                } else {
                    url += "&control=3&value=1";
                    switches.get(2).value = "1";
                    switch3ImgView.setImageResource(R.drawable.ic_check_black_24dp);
                    switch3ImgView.setBackground(getResources().getDrawable(R.drawable.green));
                }
                break;
            case R.id.cardSwitch4:
                if(switches.get(3).value.equals("1"))
                {
                    url += "&control=4&value=0";
                    switches.get(3).value = "0";
                    switch4ImgView.setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                    switch4ImgView.setBackground(getResources().getDrawable(R.drawable.grey));
                } else {
                    url += "&control=4&value=1";
                    switches.get(3).value = "1";
                    switch4ImgView.setImageResource(R.drawable.ic_check_black_24dp);
                    switch4ImgView.setBackground(getResources().getDrawable(R.drawable.green));
                }
                break;
            case R.id.cardSwitch5:
                if(switches.get(4).value.equals("1"))
                {
                    url += "&control=5&value=0";
                    switches.get(4).value = "0";
                    switch5ImgView.setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                    switch5ImgView.setBackground(getResources().getDrawable(R.drawable.grey));
                } else {
                    url += "&control=5&value=1";
                    switches.get(4).value = "1";
                    switch5ImgView.setImageResource(R.drawable.ic_check_black_24dp);
                    switch5ImgView.setBackground(getResources().getDrawable(R.drawable.green));
                }
                break;
            case R.id.cardSwitch6:
                if(switches.get(5).value.equals("1"))
                {
                    url += "&control=6&value=0";
                    switches.get(5).value = "0";
                    switch6ImgView.setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                    switch6ImgView.setBackground(getResources().getDrawable(R.drawable.grey));
                } else {
                    url += "&control=6&value=1";
                    switches.get(5).value = "1";
                    switch6ImgView.setImageResource(R.drawable.ic_check_black_24dp);
                    switch6ImgView.setBackground(getResources().getDrawable(R.drawable.green));
                }
                break;
            case R.id.cardSwitch7:
                if(switches.get(6).value.equals("1"))
                {
                    url += "&control=7&value=0";
                    switches.get(6).value = "0";
                    switch7ImgView.setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                    switch7ImgView.setBackground(getResources().getDrawable(R.drawable.grey));
                } else {
                    url += "&control=7&value=1";
                    switches.get(6).value = "1";
                    switch7ImgView.setImageResource(R.drawable.ic_check_black_24dp);
                    switch7ImgView.setBackground(getResources().getDrawable(R.drawable.green));
                }
                break;
            case R.id.cardSwitch8:
                if(switches.get(7).value.equals("1"))
                {
                    url += "&control=8&value=0";
                    switches.get(7).value = "0";
                    switch8ImgView.setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                    switch8ImgView.setBackground(getResources().getDrawable(R.drawable.grey));
                } else {
                    url += "&control=8&value=1";
                    switches.get(7).value = "1";
                    switch8ImgView.setImageResource(R.drawable.ic_check_black_24dp);
                    switch8ImgView.setBackground(getResources().getDrawable(R.drawable.green));
                }
                break;
        }

        Log.i("URL",url);

        OkHttpHandler okHttpHandler = new OkHttpHandler();
        okHttpHandler.delegate = new AfterSwitchOnCallback();
        okHttpHandler.execute(url);
    }

    @Override
    public void afterSaved() {
        _getUpdatedSettings();
    }

    public class AfterSwitchOnCallback implements AsyncResponse {

        @Override
        public void processFinish(String output) {
            // nothing yet
        }
    }

    public class AfterCheckSwitchStatusCallback implements AsyncResponse {

        @Override
        public void processFinish(String response) {

            Log.i("ProcessFinish",response);

            if(response.equals("")) {
                Toast.makeText(getApplicationContext(),"Error server response.",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                checkSwitchStatusButton.setEnabled(true);
                return;
            }

            String[] controlsKeyVal = response.split(",");

            for(String kv : controlsKeyVal) {
                if(!kv.equals("")) {
                    String[] s = kv.split("=");
                    switches.add(new Switch(s[0],s[1]));
                }
            }

            for(Switch sw : switches)
            {
                int resourceId = getResources().getIdentifier(sw.name,"id",getApplicationContext().getPackageName());

                ImageView imgView = findViewById(resourceId);

                if(sw.value.equals("1")) {
                    imgView.setImageResource(R.drawable.ic_check_black_24dp);
                    imgView.setBackground(getResources().getDrawable(R.drawable.green));
                } else {
                    imgView.setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                    imgView.setBackground(getResources().getDrawable(R.drawable.grey));
                }
            }

            progressDialog.dismiss();
            checkSwitchStatusButton.setEnabled(true);
        }
    }
}
