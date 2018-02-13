package com.darrylfernandez.homeautomation.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.darrylfernandez.homeautomation.HomeAutomation;
import com.darrylfernandez.homeautomation.R;
import com.darrylfernandez.homeautomation.adapters.ActiveSchedulesAdapter;
import com.darrylfernandez.homeautomation.adapters.SwitchArrayAdapter;
import com.darrylfernandez.homeautomation.models.Schedules;
import com.darrylfernandez.homeautomation.models.Switch;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {

    protected Switch selectedItem;

    protected HomeAutomation homeAutomation;

    protected RecyclerView activeSchedulesRecyclerView;

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

        // set the title
        setTitle("Switch Schedules");

        Spinner swSelect = findViewById(R.id.selectedSwitch);

        // switch spinner
        SwitchArrayAdapter adapter = new SwitchArrayAdapter(this, R.layout.spinner_item,homeAutomation.switches);

        swSelect.setAdapter(adapter);

        swSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (Switch) parent.getItemAtPosition(position);
                Log.i("selected item",selectedItem.name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        // active schedules recycler view
        activeSchedulesRecyclerView = findViewById(R.id.recyclerViewActiveSchedules);

        ActiveSchedulesAdapter activeSchedulesAdapter = new ActiveSchedulesAdapter(HomeAutomation.switchSchedules,this);
        RecyclerView.LayoutManager activeSchedulesLayoutManager = new LinearLayoutManager(getApplicationContext());

        activeSchedulesRecyclerView.setLayoutManager(activeSchedulesLayoutManager);
        activeSchedulesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        activeSchedulesRecyclerView.setAdapter(activeSchedulesAdapter);

        // show notice if there is no active schedules
        if(HomeAutomation.switchSchedules.isEmpty()) {
            TextView noDataTextView = findViewById(R.id.textViewNoActiveSchedules);
            noDataTextView.setVisibility(View.VISIBLE);
        }
    }

    public void scheduleSwitchButtonClick(View v) {
        Intent scheduleSwitchIntent = new Intent(this,ScheduleSwitchActivity.class);
        scheduleSwitchIntent.putExtra("switchName",selectedItem.name);
        startActivity(scheduleSwitchIntent);
    }
}
