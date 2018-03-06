package com.bus.model;

/**
 * Created by John Yan on 1/27/2017.
 */

public class locBean {
    private int icon_star;
    private String tag;
    private int icon_more;

    public locBean(int icon_star, String tag, int icon_more) {
        this.icon_star = icon_star;
        this.tag = tag;
        this.icon_more = icon_more;
    }

    public int getIcon_star() {
        return icon_star;
    }

    public void setIcon_star(int icon_star) {
        this.icon_star = icon_star;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getIcon_more() {
        return icon_more;
    }

    public void setIcon_more(int icon_more) {
        this.icon_more = icon_more;
    }
}
