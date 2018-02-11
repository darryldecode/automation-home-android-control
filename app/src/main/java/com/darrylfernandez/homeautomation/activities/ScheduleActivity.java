package com.darrylfernandez.homeautomation.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.darrylfernandez.homeautomation.HomeAutomation;
import com.darrylfernandez.homeautomation.R;
import com.darrylfernandez.homeautomation.models.Schedules;
import com.darrylfernandez.homeautomation.models.Switch;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {

    protected String selectedItem;

    protected HomeAutomation homeAutomation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        if(HomeAutomation.instance == null) {
            finish();
        } else {
            homeAutomation = HomeAutomation.instance;
            initializeActivity();
        }
    }

    public void initializeActivity() {

        Spinner swSelect = findViewById(R.id.selectedSwitch);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item,_getSwitchNames());

        swSelect.setAdapter(adapter);

        swSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);
                Log.i("selected item",selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void scheduleSwitchButtonClick(View v) {
        Intent scheduleSwitchIntent = new Intent(this,ScheduleSwitchActivity.class);
        scheduleSwitchIntent.putExtra("switchName",selectedItem);
        startActivity(scheduleSwitchIntent);
    }

    private ArrayList<String> _getSwitchNames() {

        ArrayList<String> sws = new ArrayList<>();

        for(Switch sw : homeAutomation.switches) {
            sws.add(sw.name);
        }

        return sws;
    }
}
