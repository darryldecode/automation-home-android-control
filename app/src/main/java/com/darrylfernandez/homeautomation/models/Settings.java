package com.darrylfernandez.homeautomation.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {

    // server
    public String ip = "";
    public String port = "";
    public String token = "";

    // switch alias
    public String switch1Alias = "";
    public String switch2Alias = "";
    public String switch3Alias = "";
    public String switch4Alias = "";
    public String switch5Alias = "";
    public String switch6Alias = "";
    public String switch7Alias = "";
    public String switch8Alias = "";

    // storage
    private SharedPreferences _sharedPreferences;

    // error message if there is any
    public String errorMessage = "";

    public Settings(Context c) {

        _sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);

        ip = _sharedPreferences.getString("ip", "");
        port = _sharedPreferences.getString("port", "");
        token = _sharedPreferences.getString("token", "");

        switch1Alias = _sharedPreferences.getString("switch1", "Switch 1");
        switch2Alias = _sharedPreferences.getString("switch2", "Switch 2");
        switch3Alias = _sharedPreferences.getString("switch3", "Switch 3");
        switch4Alias = _sharedPreferences.getString("switch4", "Switch 4");
        switch5Alias = _sharedPreferences.getString("switch5", "Switch 5");
        switch6Alias = _sharedPreferences.getString("switch6", "Switch 6");
        switch7Alias = _sharedPreferences.getString("switch7", "Switch 7");
        switch8Alias = _sharedPreferences.getString("switch8", "Switch 8");
    }

    public boolean save() {

        try {

            SharedPreferences.Editor editor = _sharedPreferences.edit();

            editor.putString("ip",ip);
            editor.putString("port",port);
            editor.putString("token",token);
            editor.putString("switch1",switch1Alias);
            editor.putString("switch2",switch2Alias);
            editor.putString("switch3",switch3Alias);
            editor.putString("switch4",switch4Alias);
            editor.putString("switch5",switch5Alias);
            editor.putString("switch6",switch6Alias);
            editor.putString("switch7",switch7Alias);
            editor.putString("switch8",switch8Alias);
            editor.apply();

            return true;

        } catch (Exception e) {

            errorMessage = "Failed to save settings.";
            e.printStackTrace();
            return false;
        }
    }
}
