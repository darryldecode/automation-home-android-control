package com.darrylfernandez.homeautomation.models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.darrylfernandez.homeautomation.handlers.ScheduleTriggerHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class SwitchSchedule {

    public static final String STORAGE_KEY_PREFIX = "sw_schedule_";

    public static final String HOURS_KEY = "hrs";
    public static final String MINUTES_KEY = "mins";
    public static final String ACTION_KEY = "act";

    public static final String ACTION_ON = "on";
    public static final String ACTION_OFF = "off";

    public Switch aSwitch;
    public boolean hasSchedule = false;
    public int scheduleHours = 0;
    public int scheduleMinutes = 0;
    public String action = "";
    public String errorMessage = "";
    private Context _context;

    // storage
    private SharedPreferences _sharedPreferences;

    public SwitchSchedule(){}

    public SwitchSchedule(Context c, Switch s, int hrs, int mins, String act) {
        aSwitch = s;
        scheduleHours = hrs;
        scheduleMinutes = mins;
        action = act;
        _sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        _context = c;
    }

    public static ArrayList<String> getSwitchValues() {

        ArrayList<String> s = new ArrayList<>();

        s.add(ACTION_ON);
        s.add(ACTION_OFF);

        return s;
    }

    public static SwitchSchedule get(Context c, Switch sw) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);

        String swSchedule = sp.getString(STORAGE_KEY_PREFIX + sw.name, "");

        int hrs = 0;
        int mins = 0;
        String act = "";

        if(swSchedule.equals("")) return new SwitchSchedule(c,sw,hrs,mins,act);

        // parse
        String[] controlsKeyVal = swSchedule.split(",");

        for(String kv : controlsKeyVal) {

            if(!kv.equals("")) {

                String[] i = kv.split("=");
                String k = i[0];
                String v = i[1];

                if(k.equals(HOURS_KEY)) hrs = Integer.parseInt(v);
                if(k.equals(MINUTES_KEY)) mins = Integer.parseInt(v);
                if(k.equals(ACTION_KEY)) act = v;
            }
        }

        return new SwitchSchedule(c,sw,hrs,mins,act);
    }

    public boolean save() {

        if(scheduleHours == 0 && scheduleMinutes == 0) return false;

        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putString(_buildKeyFormat(), _buildValueFormat());
        editor.apply();

        return true;
    }

    public boolean addPendingTrigger() {

        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR, scheduleHours);
        now.add(Calendar.MINUTE, scheduleMinutes);

        // the schedule trigger intent
        Intent i = new Intent(_context, ScheduleTriggerHandler.class);
        i.putExtra("switchName",aSwitch.name);
        i.putExtra("action",action);

        // pending intent
        PendingIntent pi = PendingIntent.getBroadcast(_context.getApplicationContext(),(int)System.currentTimeMillis(),i,PendingIntent.FLAG_UPDATE_CURRENT);

        // set the alarm manager
        AlarmManager alarmManager = (AlarmManager)_context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), pi);
            return true;
        } else {
            errorMessage = "Alarm Manager is null.";
            return false;
        }
    }

    private String _buildKeyFormat() {
        return STORAGE_KEY_PREFIX + aSwitch.name;
    }

    private String _buildValueFormat() {
        return HOURS_KEY + "=" + scheduleHours + "," + MINUTES_KEY + "=" + scheduleMinutes + "," + ACTION_KEY + "=" + action;
    }
}
