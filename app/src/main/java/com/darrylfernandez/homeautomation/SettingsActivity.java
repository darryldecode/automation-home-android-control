package com.darrylfernandez.homeautomation;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    protected EditText IpEditText;
    protected EditText PortEditText;
    protected EditText TokenEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String ip = preferences.getString("ip", "");
        String port = preferences.getString("port", "");
        String token = preferences.getString("token", "");

        IpEditText = findViewById(R.id.editTextIp);
        PortEditText = findViewById(R.id.editTextPort);
        TokenEditText = findViewById(R.id.editTextToken);

        // set value
        if(!ip.equals("")) IpEditText.setText(ip);
        if(!port.equals("")) PortEditText.setText(port);
        if(!token.equals("")) TokenEditText.setText(token);
    }

    public void saveClick(View v)
    {
        String ip = IpEditText.getText().toString();
        String port = PortEditText.getText().toString();
        String token = TokenEditText.getText().toString();

        if(ip.equals("") || port.equals("") || token.equals("")) {

            Toast.makeText(getApplicationContext(),"Ip, Port & Token is required",Toast.LENGTH_SHORT).show();

        } else {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("ip",ip);
            editor.putString("port",port);
            editor.putString("token",token);
            editor.apply();

            Toast.makeText(getApplicationContext(),"Settings saved!",Toast.LENGTH_SHORT).show();
        }
    }
}
