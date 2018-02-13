package com.darrylfernandez.homeautomation.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class ScheduleSwitchActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    protected String currentSwitchName;

    protected Switch currentSwitch;

    protected SwitchSchedule switchSchedule;

    protected HomeAutomation homeAutomation;

    // via choose time values
    protected int hours;
    protected int minutes;
    protected int seconds;

    // ui
    protected EditText editTextScheduleHrs;
    protected EditText editTextScheduleMins;
    protected Spinner spinnerSelectedAction;
    protected TimePickerDialog tpd = null;

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

    public void btn_selectTime(View v) {

        Calendar now = Calendar.getInstance();

        if(tpd == null) {
            tpd = TimePickerDialog.newInstance(
                    ScheduleSwitchActivity.this,
                    now.get(Calendar.HOUR),
                    now.get(Calendar.MINUTE),
                    now.get(Calendar.SECOND),
                    false
            );
        } else {
            tpd.show(getFragmentManager(), "Timepickerdialog");
        }
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        hours = hourOfDay;
        minutes = minute;
        seconds = second;

        Calendar now = Calendar.getInstance();

        now.set(Calendar.HOUR,hours);
        now.set(Calendar.MINUTE,minutes);
        now.set(Calendar.SECOND,seconds);

        // show dialog asking to add to schedule
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Add to schedule?");

        // if yes, add to schedule
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                SwitchSchedule switchSchedule = new SwitchSchedule(getApplicationContext(),currentSwitch,hours,minutes,selectedAction,true);

                if(switchSchedule.addPendingTrigger()) {
                    Toast.makeText(getApplicationContext(),"Schedule set for " + currentSwitchName, Toast.LENGTH_SHORT).show();
                    HomeAutomation.switchSchedules.add(switchSchedule);
                } else {
                    Toast.makeText(getApplicationContext(),switchSchedule.errorMessage, Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        // no, don't add
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // build the alert and show it
        AlertDialog alert = builder.create();
        alert.show();
    }
}
