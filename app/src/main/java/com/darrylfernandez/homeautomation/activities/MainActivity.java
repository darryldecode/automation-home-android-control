package com.darrylfernandez.homeautomation.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.darrylfernandez.homeautomation.HomeAutomation;
import com.darrylfernandez.homeautomation.R;
import com.darrylfernandez.homeautomation.handlers.OkHttpHandler;
import com.darrylfernandez.homeautomation.interfaces.AsyncResponse;
import com.darrylfernandez.homeautomation.interfaces.SettingsSaveCallBack;
import com.darrylfernandez.homeautomation.models.Switch;

import java.io.IOException;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SettingsSaveCallBack {

    protected OkHttpClient httpClient;
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

    protected HomeAutomation homeAutomation;

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

        // the backbone
        homeAutomation = new HomeAutomation(this);

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

        // set switch names (aliases)
        TextView sw1Label = findViewById(R.id.labelSwitch1);
        TextView sw2Label = findViewById(R.id.labelSwitch2);
        TextView sw3Label = findViewById(R.id.labelSwitch3);
        TextView sw4Label = findViewById(R.id.labelSwitch4);
        TextView sw5Label = findViewById(R.id.labelSwitch5);
        TextView sw6Label = findViewById(R.id.labelSwitch6);
        TextView sw7Label = findViewById(R.id.labelSwitch7);
        TextView sw8Label = findViewById(R.id.labelSwitch8);

        sw1Label.setText(homeAutomation.settings.switch1Alias);
        sw2Label.setText(homeAutomation.settings.switch2Alias);
        sw3Label.setText(homeAutomation.settings.switch3Alias);
        sw4Label.setText(homeAutomation.settings.switch4Alias);
        sw5Label.setText(homeAutomation.settings.switch5Alias);
        sw6Label.setText(homeAutomation.settings.switch6Alias);
        sw7Label.setText(homeAutomation.settings.switch7Alias);
        sw8Label.setText(homeAutomation.settings.switch8Alias);
    }

    private void _getSwitchStatus() {

        checkSwitchStatusButton.setEnabled(false);

        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Checking");
        progressDialog.setMessage("Checking server connection, please wait..");
        progressDialog.show();

        if(homeAutomation.hasInvalidConfig()) {
            Toast.makeText(getApplicationContext(),"Server settings needed. Check Settings.",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            checkSwitchStatusButton.setEnabled(true);
            return;
        }

        String url = homeAutomation.getSwitchStatusWebApiEndpoint();

        Log.i("URL",url);

        try {
            OkHttpHandler okHttpHandler = new OkHttpHandler();
            okHttpHandler.delegate = new AfterCheckSwitchStatusCallback();
            okHttpHandler.execute(url);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Cannot get switch status at the moment. Check Settings.",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            checkSwitchStatusButton.setEnabled(true);
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

        if (id == R.id.nav_schedule) {
            Intent ScheduleIntent = new Intent(MainActivity.this, ScheduleActivity.class);
            MainActivity.this.startActivity(ScheduleIntent);
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

        String url = homeAutomation.getSwitchCommandWebApiEndpoint();

        if(homeAutomation.switches.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please check switch status first.",Toast.LENGTH_SHORT).show();
            return;
        }

        switch (v.getId()) {

            case R.id.cardSwitch1:
                if(homeAutomation.switchIsOn(HomeAutomation.SWITCH1))
                {
                    url += homeAutomation.getCommandString("1","0");
                    homeAutomation.markSwitchAsOff(HomeAutomation.SWITCH1);
                    switch1ImgView.setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                    switch1ImgView.setBackground(getResources().getDrawable(R.drawable.grey));
                } else {
                    url += homeAutomation.getCommandString("1","1");
                    homeAutomation.markSwitchAsOn(HomeAutomation.SWITCH1);
                    switch1ImgView.setImageResource(R.drawable.ic_check_black_24dp);
                    switch1ImgView.setBackground(getResources().getDrawable(R.drawable.green));
                }
                break;
            case R.id.cardSwitch2:
                if(homeAutomation.switchIsOn(HomeAutomation.SWITCH2))
                {
                    url += homeAutomation.getCommandString("2","0");
                    homeAutomation.markSwitchAsOff(HomeAutomation.SWITCH2);
                    switch2ImgView.setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                    switch2ImgView.setBackground(getResources().getDrawable(R.drawable.grey));
                } else {
                    url += homeAutomation.getCommandString("2","1");
                    homeAutomation.markSwitchAsOn(HomeAutomation.SWITCH2);
                    switch2ImgView.setImageResource(R.drawable.ic_check_black_24dp);
                    switch2ImgView.setBackground(getResources().getDrawable(R.drawable.green));
                }
                break;
            case R.id.cardSwitch3:
                if(homeAutomation.switchIsOn(HomeAutomation.SWITCH3))
                {
                    url += homeAutomation.getCommandString("3","0");
                    homeAutomation.markSwitchAsOff(HomeAutomation.SWITCH3);
                    switch3ImgView.setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                    switch3ImgView.setBackground(getResources().getDrawable(R.drawable.grey));
                } else {
                    url += homeAutomation.getCommandString("3","1");
                    homeAutomation.markSwitchAsOn(HomeAutomation.SWITCH3);
                    switch3ImgView.setImageResource(R.drawable.ic_check_black_24dp);
                    switch3ImgView.setBackground(getResources().getDrawable(R.drawable.green));
                }
                break;
            case R.id.cardSwitch4:
                if(homeAutomation.switchIsOn(HomeAutomation.SWITCH4))
                {
                    url += homeAutomation.getCommandString("4","0");
                    homeAutomation.markSwitchAsOff(HomeAutomation.SWITCH4);
                    switch4ImgView.setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                    switch4ImgView.setBackground(getResources().getDrawable(R.drawable.grey));
                } else {
                    url += homeAutomation.getCommandString("4","1");
                    homeAutomation.markSwitchAsOn(HomeAutomation.SWITCH4);
                    switch4ImgView.setImageResource(R.drawable.ic_check_black_24dp);
                    switch4ImgView.setBackground(getResources().getDrawable(R.drawable.green));
                }
                break;
            case R.id.cardSwitch5:
                if(homeAutomation.switchIsOn(HomeAutomation.SWITCH5))
                {
                    url += homeAutomation.getCommandString("5","0");
                    homeAutomation.markSwitchAsOff(HomeAutomation.SWITCH5);
                    switch5ImgView.setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                    switch5ImgView.setBackground(getResources().getDrawable(R.drawable.grey));
                } else {
                    url += homeAutomation.getCommandString("5","1");
                    homeAutomation.markSwitchAsOn(HomeAutomation.SWITCH5);
                    switch5ImgView.setImageResource(R.drawable.ic_check_black_24dp);
                    switch5ImgView.setBackground(getResources().getDrawable(R.drawable.green));
                }
                break;
            case R.id.cardSwitch6:
                if(homeAutomation.switchIsOn(HomeAutomation.SWITCH6))
                {
                    url += homeAutomation.getCommandString("6","0");
                    homeAutomation.markSwitchAsOff(HomeAutomation.SWITCH6);
                    switch6ImgView.setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                    switch6ImgView.setBackground(getResources().getDrawable(R.drawable.grey));
                } else {
                    url += homeAutomation.getCommandString("6","1");
                    homeAutomation.markSwitchAsOn(HomeAutomation.SWITCH6);
                    switch6ImgView.setImageResource(R.drawable.ic_check_black_24dp);
                    switch6ImgView.setBackground(getResources().getDrawable(R.drawable.green));
                }
                break;
            case R.id.cardSwitch7:
                if(homeAutomation.switchIsOn(HomeAutomation.SWITCH7))
                {
                    url += homeAutomation.getCommandString("7","0");
                    homeAutomation.markSwitchAsOff(HomeAutomation.SWITCH7);
                    switch7ImgView.setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                    switch7ImgView.setBackground(getResources().getDrawable(R.drawable.grey));
                } else {
                    url += homeAutomation.getCommandString("7","1");
                    homeAutomation.markSwitchAsOn(HomeAutomation.SWITCH7);
                    switch7ImgView.setImageResource(R.drawable.ic_check_black_24dp);
                    switch7ImgView.setBackground(getResources().getDrawable(R.drawable.green));
                }
                break;
            case R.id.cardSwitch8:
                if(homeAutomation.switchIsOn(HomeAutomation.SWITCH8))
                {
                    url += homeAutomation.getCommandString("8","0");
                    homeAutomation.markSwitchAsOff(HomeAutomation.SWITCH8);
                    switch8ImgView.setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                    switch8ImgView.setBackground(getResources().getDrawable(R.drawable.grey));
                } else {
                    url += homeAutomation.getCommandString("8","1");
                    homeAutomation.markSwitchAsOn(HomeAutomation.SWITCH8);
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
                    homeAutomation.switches.add(new Switch(s[0],s[1]));
                }
            }

            for(Switch sw : homeAutomation.switches)
            {
                int resourceId = getResources().getIdentifier(sw.name,"id",getApplicationContext().getPackageName());

                ImageView imgView = findViewById(resourceId);

                if(sw.isOn()) {
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
