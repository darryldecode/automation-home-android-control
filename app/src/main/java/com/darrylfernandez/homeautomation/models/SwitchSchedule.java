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

    public static final String ACTION_ON = "on";
    public static final String ACTION_OFF = "off";

    public Switch aSwitch;
    public int scheduleHours = 0;
    public int scheduleMinutes = 0;
    public Calendar startTime;
    public String action = "";
    public String errorMessage = "";
    private Context _context;
    public PendingIntent pi;
    public int requestCode;

    public SwitchSchedule(){}

    public SwitchSchedule(Context c, Switch s, int hrs, int mins, String act) {
        aSwitch = s;
        scheduleHours = hrs;
        scheduleMinutes = mins;
        action = act;
        _context = c;
        startTime = Calendar.getInstance();
        requestCode = (int)System.currentTimeMillis();
    }

    public static ArrayList<String> getSwitchValues() {

        ArrayList<String> s = new ArrayList<>();

        s.add(ACTION_ON);
        s.add(ACTION_OFF);

        return s;
    }

    public boolean addPendingTrigger() {

        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR, scheduleHours);
        now.add(Calendar.MINUTE, scheduleMinutes);

        // the schedule trigger intent
        Intent i = new Intent(_context, ScheduleTriggerHandler.class);
        i.putExtra("switchName",aSwitch.name);
        i.putExtra("action",action);
        i.putExtra("requestCode",requestCode);

        // pending intent
        pi = PendingIntent.getBroadcast(_context.getApplicationContext(),requestCode,i,PendingIntent.FLAG_UPDATE_CURRENT);

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
}
