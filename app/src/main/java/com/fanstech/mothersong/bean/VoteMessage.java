package com.fanstech.mothersong.bean;

import java.io.Serializable;

/**
 *作者：胖胖祥
 *时间：2016/7/6 0006 下午 3:36
 *功能模块：投票信息
 */
public class VoteMessage implements Serializable{

    private String id;
    private String f_danceId;
    private String f_danceName;
    private String f_danceHead;
    private String f_cover;
    private String f_path;
    private String f_title;
    private String f_launchVoteCount;
    private String j_danceId;
    private String j_danceName;
    private String j_danceHead;
    private String j_cover;
    private String j_path;
    private String j_title;
    private String j_acceptVoteCount;
    private String time;
    private String timeStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getF_danceName() {
        return f_danceName;
    }

    public void setF_danceName(String f_danceName) {
        this.f_danceName = f_danceName;
    }

    public String getF_cover() {
        return f_cover;
    }

    public void setF_cover(String f_cover) {
        this.f_cover = f_cover;
    }

    public String getF_danceHead() {
        return f_danceHead;
    }

    public void setF_danceHead(String f_danceHead) {
        this.f_danceHead = f_danceHead;
    }

    public String getF_path() {
        return f_path;
    }

    public void setF_path(String f_path) {
        this.f_path = f_path;
    }

    public String getF_title() {
        return f_title;
    }

    public void setF_title(String f_title) {
        this.f_title = f_title;
    }

    public String getF_launchVoteCount() {
        return f_launchVoteCount;
    }

    public void setF_launchVoteCount(String f_launchVoteCount) {
        this.f_launchVoteCount = f_launchVoteCount;
    }

    public String getJ_danceName() {
        return j_danceName;
    }

    public void setJ_danceName(String j_danceName) {
        this.j_danceName = j_danceName;
    }

    public String getJ_cover() {
        return j_cover;
    }

    public void setJ_cover(String j_cover) {
        this.j_cover = j_cover;
    }

    public String getJ_danceHead() {
        return j_danceHead;
    }

    public void setJ_danceHead(String j_danceHead) {
        this.j_danceHead = j_danceHead;
    }

    public String getJ_path() {
        return j_path;
    }

    public void setJ_path(String j_path) {
        this.j_path = j_path;
    }

    public String getJ_acceptVoteCount() {
        return j_acceptVoteCount;
    }

    public void setJ_acceptVoteCount(String j_acceptVoteCount) {
        this.j_acceptVoteCount = j_acceptVoteCount;
    }

    public String getJ_title() {
        return j_title;
    }

    public void setJ_title(String j_title) {
        this.j_title = j_title;
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

    public String getF_danceId() {
        return f_danceId;
    }

    public void setF_danceId(String f_danceId) {
        this.f_danceId = f_danceId;
    }

    public String getJ_danceId() {
        return j_danceId;
    }

    public void setJ_danceId(String j_danceId) {
        this.j_danceId = j_danceId;
    }
}
