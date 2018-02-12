package com.darrylfernandez.homeautomation.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.darrylfernandez.homeautomation.HomeAutomation;
import com.darrylfernandez.homeautomation.R;
import com.darrylfernandez.homeautomation.models.SwitchSchedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ActiveSchedulesAdapter extends RecyclerView.Adapter {

    private List<SwitchSchedule> _switchSchedules;
    private final Context _context;
    private final ActiveSchedulesAdapter _self;

    public ActiveSchedulesAdapter(List<SwitchSchedule> switchSchedules, Context c) {
        _switchSchedules = switchSchedules;
        _context = c;
        _self = this;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView switchName;
        private TextView time;
        private TextView timeRemaining;
        private Button remove;

        private MyViewHolder(View itemView) {
            super(itemView);
            switchName = itemView.findViewById(R.id.textViewActiveScheduleSwitchName);
            time = itemView.findViewById(R.id.textViewActiveScheduleTime);
            timeRemaining = itemView.findViewById(R.id.textViewActiveScheduleTimeRemaining);
            remove = itemView.findViewById(R.id.buttonActiveScheduleRemove);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.active_schedule_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final SwitchSchedule switchSchedule = _switchSchedules.get(position);
        MyViewHolder mvh = (MyViewHolder)holder;

        // setup pretty time
        Calendar sched = (Calendar) switchSchedule.startTime.clone();
        sched.add(Calendar.HOUR,switchSchedule.scheduleHours);
        sched.add(Calendar.MINUTE,switchSchedule.scheduleMinutes);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy 'at' h:mm a");

        String dateOfAction = dateFormat.format(sched.getTime());
        String remaining = String.valueOf(TimeUnit.MILLISECONDS.toMinutes((sched.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()))) + " mins Remaining";
        String sName = switchSchedule.aSwitch.alias + " \"" + switchSchedule.action.toUpperCase() + "\"";

        mvh.switchName.setText(sName);
        mvh.time.setText(dateOfAction);
        mvh.timeRemaining.setText(remaining);
        mvh.remove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(_context);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // delete the pending schedule
                        // and cancel the alarm manager pending intent
                        switchSchedule.pi.cancel();
                        HomeAutomation.removeSwitchSchedule(switchSchedule.aSwitch);
                        _self.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return _switchSchedules.size();
    }
}
