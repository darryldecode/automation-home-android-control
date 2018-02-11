package com.darrylfernandez.homeautomation.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class SwitchSchedule {

    public static final String STORAGE_KEY_PREFIX = "sw_schedule_";

    public static final String HOURS_KEY = "hrs";
    public static final String MINUTES_KEY = "mins";

    public Switch aSwitch;
    public boolean hasSchedule = false;
    public int scheduleHours = 0;
    public int scheduleMinutes = 0;

    // storage
    private SharedPreferences _sharedPreferences;

    public SwitchSchedule(){}

    public SwitchSchedule(Context c, Switch s, int hrs, int mins) {
        aSwitch = s;
        scheduleHours = hrs;
        scheduleMinutes = mins;
        _sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
    }

    public static SwitchSchedule get(Context c, Switch sw) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);

        String swSchedule = sp.getString(STORAGE_KEY_PREFIX + sw.name, "");

        int hrs = 0;
        int mins = 0;

        if(swSchedule.equals("")) return new SwitchSchedule(c,sw,hrs,mins);

        // parse
        String[] controlsKeyVal = swSchedule.split(",");

        for(String kv : controlsKeyVal) {

            if(!kv.equals("")) {

                String[] i = kv.split("=");
                String k = i[0];
                String v = i[1];

                if(k.equals(HOURS_KEY)) hrs = Integer.parseInt(v);
                if(k.equals(MINUTES_KEY)) mins = Integer.parseInt(v);
            }
        }

        return new SwitchSchedule(c,sw,hrs,mins);
    }

    public boolean save() {

        if(scheduleHours == 0 && scheduleMinutes == 0) return false;

        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putString(_buildKeyFormat(), _buildValueFormat());
        editor.apply();

        return true;
    }

    private String _buildKeyFormat() {
        return STORAGE_KEY_PREFIX + aSwitch.name;
    }

    private String _buildValueFormat() {
        return HOURS_KEY + "=" + scheduleHours + "," + MINUTES_KEY + "=" + scheduleMinutes;
    }
}
