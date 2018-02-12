package com.darrylfernandez.homeautomation;


import android.content.Context;

import com.darrylfernandez.homeautomation.models.Settings;
import com.darrylfernandez.homeautomation.models.Switch;
import com.darrylfernandez.homeautomation.models.SwitchSchedule;

import java.util.ArrayList;
import java.util.Iterator;

public class HomeAutomation {

    // app version
    public static final String APP_NAME = "Home Automation";
    public static final String VERSION = "v0.0.3";

    // values
    public static final String VALUE_ON = "1";
    public static final String VALUE_OFF = "1";

    // switch ids
    public static final String SWITCH1 = "control1";
    public static final String SWITCH2 = "control2";
    public static final String SWITCH3 = "control3";
    public static final String SWITCH4 = "control4";
    public static final String SWITCH5 = "control5";
    public static final String SWITCH6 = "control6";
    public static final String SWITCH7 = "control7";
    public static final String SWITCH8 = "control8";

    // config
    public Settings settings;

    // the available switches
    public ArrayList<Switch> switches = new ArrayList<>();

    // the active running schedules
    public static ArrayList<SwitchSchedule> switchSchedules = new ArrayList<>();

    // instance
    public static HomeAutomation instance = null;

    // constructor
    public HomeAutomation(Context c) {
        settings = new Settings(c);
        instance = this;
    }

    // check if we have a valid server config
    public boolean hasInvalidConfig() {
        return (settings.ip.equals("") || settings.port.equals("") || settings.token.equals(""));
    }

    // the switch status web api endpoint
    public String getSwitchStatusWebApiEndpoint() {
        return "http://" + settings.ip + ":" + settings.port + "/status";
    }

    // the switch commander web api endpoint
    public String getSwitchCommandWebApiEndpoint() {
        return "http://" + settings.ip + ":" + settings.port + "?token=" + settings.token;
    }

    // determine if a switch is on
    public boolean switchIsOn(String switchName) {

        boolean isOn = false;
        int i = 0;

        for(Switch sw : switches) {
            if(sw.name.equals(switchName)) {
                isOn = switches.get(i).value.equals("1");
            }
            i++;
        }

        return isOn;
    }

    // mark the switch as ON
    public void markSwitchAsOn(String switchName) {

        int i = 0;

        for(Switch sw : switches) {
            if(sw.name.equals(switchName)) {
                switches.get(i).value = VALUE_ON;
            }
            i++;
        }
    }

    // mark switch as Off
    public void markSwitchAsOff(String switchName) {

        int i = 0;

        for(Switch sw : switches) {
            if(sw.name.equals(switchName)) {
                switches.get(i).value = VALUE_OFF;
            }
            i++;
        }
    }

    // get the command string
    public String getCommandString(String switchNumber, String switchValue) {
        return "&control=" + switchNumber +"&value=" + switchValue;
    }

    // get a switch by name
    public Switch getSwitch(String switchName) {

        Switch s = null;

        for(Switch sw : switches) {
            if(sw.name.equals(switchName)) {
                s = sw;
            }
        }

        return s;
    }

    // remove the switch schedule on the running schedules
    public static void removeSwitchSchedule(Switch sw) {

        Iterator<SwitchSchedule> i = switchSchedules.iterator();

        while (i.hasNext()) {

            SwitchSchedule currSwSched = i.next();

            if(currSwSched.aSwitch.name.equals(sw.name)) i.remove();
        }
    }
}
