package com.darrylfernandez.homeautomation.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.darrylfernandez.homeautomation.R;
import com.darrylfernandez.homeautomation.interfaces.SettingsSaveCallBack;
import com.darrylfernandez.homeautomation.models.Settings;

public class SettingsActivity extends AppCompatActivity {

    public static SettingsSaveCallBack delegate = null;

    private Settings _settings;

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

        _settings = new Settings(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // get the values from preference
        String ip = _settings.ip;
        String port = _settings.port;
        String token = _settings.token;

        String sw1 = _settings.switch1Alias;
        String sw2 = _settings.switch2Alias;
        String sw3 = _settings.switch3Alias;
        String sw4 = _settings.switch4Alias;
        String sw5 = _settings.switch5Alias;
        String sw6 = _settings.switch6Alias;
        String sw7 = _settings.switch7Alias;
        String sw8 = _settings.switch8Alias;

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

            _settings.ip = ip;
            _settings.port = port;
            _settings.token = token;
            _settings.switch1Alias = sw1;
            _settings.switch2Alias = sw2;
            _settings.switch3Alias = sw3;
            _settings.switch4Alias = sw4;
            _settings.switch5Alias = sw5;
            _settings.switch6Alias = sw6;
            _settings.switch7Alias = sw7;
            _settings.switch8Alias = sw8;

            if(!_settings.save()) {
                Toast.makeText(getApplicationContext(),_settings.errorMessage,Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),"Settings saved!",Toast.LENGTH_SHORT).show();
            }

            delegate.afterSaved();
        }
    }
}
