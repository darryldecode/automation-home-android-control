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

        if(HomeAutomation.instance != null) {

            // get the main home automation instance
            HomeAutomation homeAutomation = HomeAutomation.instance;

            // get intent params
            String action = intent.getStringExtra("action");
            String switchName = intent.getStringExtra("switchName");
            int requestCode = intent.getIntExtra("requestCode",0);

            final String act = action.equals("on") ? "1" : "0";
            Switch aSwitch = homeAutomation.getSwitch(switchName);

            // show toast
            Toast.makeText(context,aSwitch.alias + " " + action.toUpperCase(),Toast.LENGTH_LONG).show();

            // build the web api endpoint
            String url = homeAutomation.getSwitchCommandWebApiEndpoint();
            url += homeAutomation.getCommandString(String.valueOf(aSwitch.name.charAt(aSwitch.name.length() - 1)),act);

            Log.i(TAG,url);

            OkHttpHandler okHttpHandler = new OkHttpHandler();
            okHttpHandler.execute(url);

            // clean up
            // remove the running schedule on our list or running schedules
            HomeAutomation.removeSwitchScheduleByRequestCode(requestCode);

        } else {
            Log.i(TAG,"No Home Automation instance");
        }
    }
}
