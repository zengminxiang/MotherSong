package com.fanstech.mothersong.bean;

import java.io.Serializable;

/**
 *作者：胖胖祥
 *时间：2016/6/23 0023 上午 10:54
 *功能模块：视频评论
 */
public class VideoComment implements Serializable{

    private String id;
    private String videoId;
    private String myUserId;
    private String heUserId;
    private String content;
    private String time;
    private String timeStr;
    private VideoCommentUser user;
    private VideoCommentUser heUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMyUserId() {
        return myUserId;
    }

    public void setMyUserId(String myUserId) {
        this.myUserId = myUserId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeUserId() {
        return heUserId;
    }

    public void setHeUserId(String heUserId) {
        this.heUserId = heUserId;
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

    public VideoCommentUser getUser() {
        return user;
    }

    public void setUser(VideoCommentUser user) {
        this.user = user;
    }

    public VideoCommentUser getHeUser() {
        return heUser;
    }

    public void setHeUser(VideoCommentUser heUser) {
        this.heUser = heUser;
    }
}
