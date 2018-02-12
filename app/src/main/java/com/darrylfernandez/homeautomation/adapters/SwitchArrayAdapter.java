package com.darrylfernandez.homeautomation.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.darrylfernandez.homeautomation.R;
import com.darrylfernandez.homeautomation.models.Switch;

import java.util.List;

public class SwitchArrayAdapter extends ArrayAdapter {

    private List _objects;
    private Context _context;

    public SwitchArrayAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);

        _context = context;
        _objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return _getCustomView(position, convertView, parent);
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return _getCustomView(position, convertView, parent);
    }

    private View _getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );

        assert inflater != null;

        View row = inflater.inflate(R.layout.spinner_item, parent, false);

        TextView label = row.findViewById(R.id.spinnerItem);
        Switch sw = (Switch)_objects.get(position);

        label.setText(sw.alias);

        return row;
    }
}
