package com.darrylfernandez.homeautomation.handlers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.darrylfernandez.homeautomation.HomeAutomation;
import com.darrylfernandez.homeautomation.models.Switch;

public class ScheduleTriggerHandler extends BroadcastReceiver {

    static String TAG = "ScheduleTriggerHandler";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("SCHEDULE TRIGGER","got it");
        Toast.makeText(context,"Triggered..",Toast.LENGTH_LONG).show();

        if(HomeAutomation.instance != null) {

            // get the main home automation instance
            HomeAutomation homeAutomation = HomeAutomation.instance;

            // get intent params
            String action = intent.getStringExtra("action");
            String switchName = intent.getStringExtra("switchName");

            Log.i(TAG ," parameters--");
            Log.i(TAG ,action);
            Log.i(TAG ,switchName);
            Log.i(TAG ," end parameters--");

            final String act = action.equals("on") ? "1" : "0";
            Switch aSwitch = homeAutomation.getSwitch(switchName);

            Log.i(TAG,aSwitch.name);

            // build the web api endpoint
            String url = homeAutomation.getSwitchCommandWebApiEndpoint();
            url += homeAutomation.getCommandString(String.valueOf(aSwitch.name.charAt(aSwitch.name.length() - 1)),act);

            Log.i(TAG,url);

            OkHttpHandler okHttpHandler = new OkHttpHandler();
            okHttpHandler.execute(url);
        } else {
            Log.i(TAG,"No Home Automation instance");
        }
    }
}
