package com.fanstech.mothersong.bean;

import java.io.Serializable;

/**
 *作者：胖胖祥
 *时间：2016/6/29 0029 上午 10:11
 *功能模块：关注的舞队
 */
public class FollowDance implements Serializable{

    private String id;
    private String userId;
    private String danceId;
    private String danceName;
    private String danceHead;
    private String time;

    public String getId() {
        return id;
    }

    public String getDanceId() {
        return danceId;
    }

    public String getDanceName() {
        return danceName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDanceHead() {
        return danceHead;
    }

    public String getTime() {
        return time;
    }
}
