package com.fanstech.mothersong.bean;

import java.io.Serializable;

/**
 *作者：胖胖祥
 *时间：2016/7/6 0006 上午 11:29
 *功能模块：pk列表信息
 */
public class LaunchMessage implements Serializable{

    private String id;
    private String time;
    private String timeStr;
    private String state;
    private String danceName;
    private String danceHead;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDanceName() {
        return danceName;
    }

    public void setDanceName(String danceName) {
        this.danceName = danceName;
    }

    public String getDanceHead() {
        return danceHead;
    }

    public void setDanceHead(String danceHead) {
        this.danceHead = danceHead;
    }
}
