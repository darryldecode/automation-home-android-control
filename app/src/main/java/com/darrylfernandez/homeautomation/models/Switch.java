package com.darrylfernandez.homeautomation.models;

public class Switch {

    public String name;
    public String value;
    public String alias;
    public SwitchSchedule switchSchedule = null;

    public Switch(){}

    public Switch(String n, String v) {
        name = n;
        value = v;
    }

    public boolean isOn() {
        return value.equals("1");
    }
}
