package com.bus.model;

/**
 * Created by John Yan on 4/22/2017.
 */

public class stationBean {
    private int icon_loc;
    private String keyname;

    public stationBean(int icon_loc, String address, String keyname) {
        this.icon_loc = icon_loc;
        this.address = address;
        this.keyname = keyname;
    }

    private String address;

    public int getIcon_loc() {
        return icon_loc;
    }

    public void setIcon_loc(int icon_loc) {
        this.icon_loc = icon_loc;
    }

    public String getKeyname() {
        return keyname;
    }

    public void setKeyname(String keyname) {
        this.keyname = keyname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
