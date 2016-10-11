package com.fanstech.mothersong.bean;

import java.io.Serializable;

/**
 *作者：胖胖祥
 *时间：2016/7/4 0004 下午 2:56
 *功能模块：舞队队员列表信息
 */
public class DanceUserMessage implements Serializable{

    private String userId;
    private String danceId;
    private String userName;
    private String userHead;
    private String time;
    private String timeStr;
    private String state;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getDanceId() {
        return danceId;
    }

    public void setDanceId(String danceId) {
        this.danceId = danceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }
}
