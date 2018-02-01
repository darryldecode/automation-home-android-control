package com.darrylfernandez.homeautomation;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    public static SettingsSaveCallBack delegate = null;

    // server settings
    protected EditText IpEditText;
    protected EditText PortEditText;
    protected EditText TokenEditText;

    // switch names
    protected EditText switch1;
    protected EditText switch2;
    protected EditText switch3;
    protected EditText switch4;
    protected EditText switch5;
    protected EditText switch6;
    protected EditText switch7;
    protected EditText switch8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // get the values from preference
        String ip = preferences.getString("ip", "");
        String port = preferences.getString("port", "");
        String token = preferences.getString("token", "");

        String sw1 = preferences.getString("switch1", "Switch 1");
        String sw2 = preferences.getString("switch2", "Switch 2");
        String sw3 = preferences.getString("switch3", "Switch 3");
        String sw4 = preferences.getString("switch4", "Switch 4");
        String sw5 = preferences.getString("switch5", "Switch 5");
        String sw6 = preferences.getString("switch6", "Switch 6");
        String sw7 = preferences.getString("switch7", "Switch 7");
        String sw8 = preferences.getString("switch8", "Switch 8");

        // set the ui elements
        IpEditText = findViewById(R.id.editTextIp);
        PortEditText = findViewById(R.id.editTextPort);
        TokenEditText = findViewById(R.id.editTextToken);

        switch1 = findViewById(R.id.editTextSwitch1);
        switch2 = findViewById(R.id.editTextSwitch2);
        switch3 = findViewById(R.id.editTextSwitch3);
        switch4 = findViewById(R.id.editTextSwitch4);
        switch5 = findViewById(R.id.editTextSwitch5);
        switch6 = findViewById(R.id.editTextSwitch6);
        switch7 = findViewById(R.id.editTextSwitch7);
        switch8 = findViewById(R.id.editTextSwitch8);

        // set value
        if(!ip.equals("")) IpEditText.setText(ip);
        if(!port.equals("")) PortEditText.setText(port);
        if(!token.equals("")) TokenEditText.setText(token);

        if(!sw1.equals("")) switch1.setText(sw1);
        if(!sw2.equals("")) switch2.setText(sw2);
        if(!sw3.equals("")) switch3.setText(sw3);
        if(!sw4.equals("")) switch4.setText(sw4);
        if(!sw5.equals("")) switch5.setText(sw5);
        if(!sw6.equals("")) switch6.setText(sw6);
        if(!sw7.equals("")) switch7.setText(sw7);
        if(!sw8.equals("")) switch8.setText(sw8);
    }

    public void saveClick(View v)
    {
        // get the input values
        String ip = IpEditText.getText().toString();
        String port = PortEditText.getText().toString();
        String token = TokenEditText.getText().toString();

        String sw1 = switch1.getText().toString();
        String sw2 = switch2.getText().toString();
        String sw3 = switch3.getText().toString();
        String sw4 = switch4.getText().toString();
        String sw5 = switch5.getText().toString();
        String sw6 = switch6.getText().toString();
        String sw7 = switch7.getText().toString();
        String sw8 = switch8.getText().toString();

        if(ip.equals("") || port.equals("") || token.equals("")) {

            Toast.makeText(getApplicationContext(),"Ip, Port & Token is required",Toast.LENGTH_SHORT).show();

        } else {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("ip",ip);
            editor.putString("port",port);
            editor.putString("token",token);
            editor.putString("switch1",sw1);
            editor.putString("switch2",sw2);
            editor.putString("switch3",sw3);
            editor.putString("switch4",sw4);
            editor.putString("switch5",sw5);
            editor.putString("switch6",sw6);
            editor.putString("switch7",sw7);
            editor.putString("switch8",sw8);
            editor.apply();

            Toast.makeText(getApplicationContext(),"Settings saved!",Toast.LENGTH_SHORT).show();

            delegate.afterSaved();
        }
    }
}
