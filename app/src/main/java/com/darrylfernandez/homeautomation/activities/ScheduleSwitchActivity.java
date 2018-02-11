package com.darrylfernandez.homeautomation.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.darrylfernandez.homeautomation.HomeAutomation;
import com.darrylfernandez.homeautomation.R;
import com.darrylfernandez.homeautomation.models.Switch;
import com.darrylfernandez.homeautomation.models.SwitchSchedule;

public class ScheduleSwitchActivity extends AppCompatActivity {

    protected String currentSwitchName;

    protected Switch currentSwitch;

    protected SwitchSchedule switchSchedule;

    protected HomeAutomation homeAutomation;

    // ui
    protected EditText editTextScheduleHrs;
    protected EditText editTextScheduleMins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_switch);

        // call ui only once
        editTextScheduleHrs = findViewById(R.id.editTextScheduleHours);
        editTextScheduleMins = findViewById(R.id.editTextScheduleMinutes);

        Intent intent = getIntent();

        currentSwitchName = intent.getStringExtra("switchName");

        if(HomeAutomation.instance == null) {
            finish();
        } else {
            homeAutomation = HomeAutomation.instance;
            initializeActivity();
        }
    }

    protected void initializeActivity() {

        Log.i("Current Switch Name",currentSwitchName);

        // get the current switch instance
        currentSwitch = homeAutomation.getSwitch(currentSwitchName);

        // set the title
        setTitle("Schedule for " + currentSwitch.name);

        // set value if exist
        switchSchedule = SwitchSchedule.get(this,currentSwitch);

        Log.i("schedule hours: ",String.valueOf(switchSchedule.scheduleHours));
        Log.i("schedule minutes: ",String.valueOf(switchSchedule.scheduleMinutes));

        editTextScheduleHrs.setText(String.valueOf(switchSchedule.scheduleHours));
        editTextScheduleMins.setText(String.valueOf(switchSchedule.scheduleMinutes));
    }

    public void btn_saveSwitchScheduleClick(View v) {

        int hrs = Integer.parseInt(editTextScheduleHrs.getText().toString());
        int mins = Integer.parseInt(editTextScheduleMins.getText().toString());

        SwitchSchedule switchSchedule = new SwitchSchedule(this,currentSwitch,hrs,mins);

        if(switchSchedule.save()) {
            Toast.makeText(getApplicationContext(),"Schedule set for " + currentSwitchName, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),"Failed to set schedule for " + currentSwitchName, Toast.LENGTH_SHORT).show();
        }
    }
}
