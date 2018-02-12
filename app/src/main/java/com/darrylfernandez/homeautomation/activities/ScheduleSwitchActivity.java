package com.darrylfernandez.homeautomation.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
    protected Spinner spinnerSelectedAction;

    String selectedAction = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_switch);

        // call ui only once
        editTextScheduleHrs = findViewById(R.id.editTextScheduleHours);
        editTextScheduleMins = findViewById(R.id.editTextScheduleMinutes);
        spinnerSelectedAction = findViewById(R.id.spinnerSelectedAction);

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
        setTitle("Schedule for " + currentSwitch.alias);

        // action dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item,SwitchSchedule.getSwitchValues());
        spinnerSelectedAction.setAdapter(adapter);
        spinnerSelectedAction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAction = (String) parent.getItemAtPosition(position);
                Log.i("selected action",selectedAction);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void btn_saveSwitchScheduleClick(View v) {

        int hrs = Integer.parseInt(editTextScheduleHrs.getText().toString());
        int mins = Integer.parseInt(editTextScheduleMins.getText().toString());
        String act = selectedAction;

        SwitchSchedule switchSchedule = new SwitchSchedule(this,currentSwitch,hrs,mins,act);

        if(switchSchedule.addPendingTrigger()) {

            Toast.makeText(getApplicationContext(),"Schedule set for " + currentSwitchName, Toast.LENGTH_SHORT).show();

            HomeAutomation.switchSchedules.add(switchSchedule);
        } else {
            Toast.makeText(getApplicationContext(),switchSchedule.errorMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
