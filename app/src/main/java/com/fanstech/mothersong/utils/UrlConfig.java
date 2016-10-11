package com.fanstech.mothersong.utils;


import java.util.Map;

/**
 *作者：胖胖祥
 *时间：2016/6/14 0014 上午 9:28
 *功能模块：api
 */
public class UrlConfig {

    public static Map<String,String> params;
    public static String URL = "http://120.25.172.152";
    public static String REGISTER = URL+"/register.do";//注册
    public static String ISREGISTER = URL+"/getIdByAccount.do";//判断手机号是否登录
    public static String LOGIN = URL+"/login.do";//登录
    public static String FRGET = URL+"/updatePassword.do";//修改密码
    public static String USERMESSAGE = URL+"/u/info.do";//获取用户信息
    public static String WEIXIN = URL+"/loginByWeChart.do";//微信登录
    public static String USERHEAD = URL +"/u/uploadHead.do";//修改头像
    public static String ALTERNAME = URL +"/u/updateNickname.do";//修改昵称
    public static String SELECTNAME = URL +"/checkNicknameExist.do";//查询昵称是否使用了


    public static String VIDEOLIST = URL+"/getVideo.do";//获取视频列表
    public static String UPLOADVIDEO = URL + "/u/uploadVideo.do";//上传视频
    public static String GETCOUMMENT = URL + "/getVideoComment.do";//获取视频评论
    public static String ADDCOUMMENT = URL + "/u/addComment.do";//添加视频评论
    public static String WHETHERZAMBIA = URL + "/u/queryGood.do";//判断是否已经点赞了
    public static String WHETHERCOLLECTION = URL + "/u/queryFavorite.do";//判断是否已经收藏
    public static String ADDZAMBIA = URL + "/u/addGood.do";//点赞
    public static String ADDCOLLECTION = URL + "/u/addFavorite.do";//收藏
    public static String ADDBROWSER = URL + "/addBrowser.do";//添加播放量

    public static String ADMIN = URL +"/u/checkDanceManager.do";//判断用户是否是舞队管理员
    public static String WHETHERDANCE = URL + "/u/queryDance.do";//判断是否加入舞队
    public static String WHETHUSER = URL +"/u/getMyCreateDance.do";//判断是否创建舞队
    public static String DANCEADDRE = URL +"/getSquareByArea.do";//获取主场名称
    public static String ESTABLISHDANCE = URL +"/u/addDance.do";//创建舞队
    public static String GETMYDANCE = URL +"/u/getMyDance.do";//查询我的舞队信息
    public static String SELECTDANCE = URL +"/getDanceBySquareId.do";//查询主场下的舞队
    public static String GETMYVIDEO = URL +"/u/myVideos.do";//我的视频
    public static String GETCOLLECTION = URL + "/u/myFavorite.do";//我的收藏
    public static String ADDDANCE = URL +"/u/appleyJoinDance.do";//申请加入舞队
    public static String ADDFOLLOW = URL +"/u/addFollow.do";//关注舞队
    public static String SELECTFOLLOW = URL +"/u/myFollowDance.do";//关注舞队的列表
    public static String GETDANCESQMESSAGE = URL +"/u/unCheckedApply.do";//申请加入舞队列表
    public static String GETMEMBER = URL +"/u/getMembers.do";//队员列表
    public static String ENCLOSURELIST = URL +"/getSquare.do";//获取附件的舞队 ToExamine
    public static String TOEXAMINE = URL +"/u/checkJoinDance.do";//同意或拒绝某人加入舞队

    public static String SUBMITLAUNCH = URL +"/u/startPk.do";//发起pk
    public static String MYLAUNCHMESSAGE = URL +"/u/getLaunchPk.do";//我发起pk列表
    public static String COVERLAUNCHMESSAGE = URL +"/u/getAcceptPk.do";//被发起pk列表
    public static String COVERPK = URL +"/u/acceptUploadVideo.do";//接受pk，上传视频
    public static String VOTELIST = URL +"/getVotePage.do";//投票列表
    public static String PKYES = URL +"/u/processPkApply.do";//接受pk
    public static String VOTEPIAO = URL +"/u/addVote.do";//投票

}
