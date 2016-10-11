package com.fanstech.mothersong.bean;

import java.io.Serializable;

/**
 *作者：胖胖祥
 *时间：2016/6/23 0023 上午 10:57
 *功能模块：发表评论的用户
 */
public class VideoCommentUser implements Serializable{

    private String head;
    private String nickname;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


}
