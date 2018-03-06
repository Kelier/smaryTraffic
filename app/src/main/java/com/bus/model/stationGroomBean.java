package com.bus.model;

/**
 * Created by John Yan on 1/27/2017.
 */

public class stationGroomBean {
//    private int station_icon;//车站图标
    private String station_now;//所在车站
    private String station_distance;//车站距离
    private String station_route;//搭乘公交
    private String station_next;//下一站
    private int station_degree;//拥挤程度
    private String station_time;//出发时间

    public String getStation_now() {
        return station_now;
    }

    public void setStation_now(String station_now) {
        this.station_now = station_now;
    }

    public String getStation_distance() {
        return station_distance;
    }

    public void setStation_distance(String station_distance) {
        this.station_distance = station_distance;
    }

    public String getStation_route() {
        return station_route;
    }

    public void setStation_route(String station_route) {
        this.station_route = station_route;
    }

    public String getStation_next() {
        return station_next;
    }

    public void setStation_next(String station_next) {
        this.station_next = station_next;
    }

    public int getStation_degree() {
        return station_degree;
    }

    public void setStation_degree(int station_degree) {
        this.station_degree = station_degree;
    }

    public String getStation_time() {
        return station_time;
    }

    public void setStation_time(String station_time) {
        this.station_time = station_time;
    }

    public stationGroomBean(String station_now, String station_distance, String station_route, String station_next, int station_degree, String station_time) {
        this.station_now = station_now;
        this.station_distance = station_distance;
        this.station_route = station_route;
        this.station_next = station_next;
        this.station_degree = station_degree;
        this.station_time = station_time;
    }
}
