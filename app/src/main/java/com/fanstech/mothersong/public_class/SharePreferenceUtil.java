package com.fanstech.mothersong.public_class;


import android.content.Context;
import android.content.SharedPreferences;

/**
 *作者：胖胖祥
 *时间：2016/6/14 0014 上午 10:26
 *功能模块：重写SharePreference
 */
public class SharePreferenceUtil {

    private final String SHAREDPRE_FILE_NAME = "UserMessage"; // 配置文件名

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    /**
     * 单例对象实例
     */
    private static SharePreferenceUtil instance = null;

    public static SharePreferenceUtil getInstance(Context mContext) {
        if (instance == null) {
            synchronized (SharePreferenceUtil.class) {
                if (instance == null) {
                    instance = new SharePreferenceUtil(mContext);
                }
            }
        }
        return instance;
    }

    /**
     * 构造函数
     * @param mContext：上下文环境
     */
    public SharePreferenceUtil(Context mContext) {
        sp = mContext.getSharedPreferences(SHAREDPRE_FILE_NAME,Context.MODE_PRIVATE);
        editor = sp.edit();
    }
    /**
     * 清除本地保存的所有数据
     */
    public void clear(){
        editor.clear();
        editor.commit();
    }

    /**
     * 保存数据到本地
     * @param key 键
     * @param value 值 Object:目前只支持：String/Boolean/Float/Integer/Long
     */
    public void saveKeyObjValue(String key,Object value){
        if(value instanceof String){
            editor.putString(key, (String)value);
        }else if(value instanceof Boolean){
            editor.putBoolean(key, (Boolean) value);
        }else if(value instanceof Float){
            editor.putFloat(key, (Float) value);
        }else if(value instanceof Integer){
            editor.putInt(key, (Integer) value);
        }else if(value instanceof Long){
            editor.putLong(key, (Long) value);
        }
        editor.commit();
    }

    /**
     * 获取保存在本地的数据 未加密的
     * @param key
     * @param defValue
     * @return String
     */
    public String getString(String key, String defValue){
        return sp.getString(key, defValue);
    }
    /**
     * 获取保存在本地的数据 未加密的
     * @param key
     * @param defValue
     * @return Boolean
     */
    public boolean getBoolean(String key, boolean defValue){
        return sp.getBoolean(key, defValue);
    }

    public static final String id = "id";
    public static final String nickname = "nickname";
    public static final String account = "account";
    public static final String password = "password";
    public static final String introduce = "introduce";
    public static final String head = "head";
    public static final String grade = "grade";
    public static final String province = "province";
    public static final String city = "city";
    public static final String openid = "openid";
    public static final String accountType = "accountType";
    public static final String state = "state";
    public static final String time = "time";
    public static final String timeStr = "timeStr";
    public static final String token = "token";
    public static final String danceID = "danceId";
    public static final String adminDance = "admin";
    public static final String loading = "loading";


//    public String getToken() {
//        return sp.getString("token", "");
//    }
//
//    public void setToken(String token) {
//        editor.putString("token", token);
//        editor.commit();
//    }

//    public String getId() {
//        return sp.getString("id","");
//    }
//
//    public void setId(String id) {
//        editor.putString("id", id);
//        editor.commit();
//    }
//
//    public String getNickname() {
//        return sp.getString("nickname","");
//    }
//
//    public void setNickname(String nickname) {
//        editor.putString("nickname", nickname);
//        editor.commit();
//    }
//
//    public String getAccount() {
//        return sp.getString("account","");
//    }
//
//    public void setAccount(String account) {
//        editor.putString("account", account);
//        editor.commit();
//    }
//
//    public String getPassword() {
//        return sp.getString("password","");
//    }
//
//    public void setPassword(String password) {
//        editor.putString("password", password);
//        editor.commit();
//    }
//
//    public String getIntroduce() {
//        return sp.getString("introduce","");
//    }
//
//    public void setIntroduce(String introduce) {
//        editor.putString("introduce", introduce);
//        editor.commit();
//    }
//
//    public String getHead() {
//        return sp.getString("head","");
//    }
//
//    public void setHead(String head) {
//        editor.putString("head", head);
//        editor.commit();
//    }
//
//    public String getGrade() {
//        return sp.getString("grade","");
//    }
//
//    public void setGrade(String grade) {
//        editor.putString("grade", grade);
//        editor.commit();
//    }
//
//    public String getProvince() {
//        return sp.getString("province","");
//    }
//
//    public void setProvince(String province) {
//        editor.putString("province", province);
//        editor.commit();
//    }
//
//    public String getCity() {
//        return sp.getString("city","");
//    }
//
//    public void setCity(String city) {
//        editor.putString("city", city);
//        editor.commit();
//    }
//
//    public String getOpenid() {
//        return sp.getString("openid","");
//    }
//
//    public void setOpenid(String openid) {
//        editor.putString("openid", openid);
//        editor.commit();
//    }
//
//    public String getAccountType() {
//        return sp.getString("accountType","");
//    }
//
//    public void setAccountType(String accountType) {
//        editor.putString("accountType", accountType);
//        editor.commit();
//    }
//
//    public String getState() {
//        return sp.getString("state","");
//    }
//
//    public void setState(String state) {
//        editor.putString("state", state);
//        editor.commit();
//    }
//
//    public String getTime() {
//        return sp.getString("time","");
//    }
//
//    public void setTime(String time) {
//        editor.putString("time", time);
//        editor.commit();
//    }
//
//    public String getTimeStr() {
//        return sp.getString("timeStr","");
//    }
//
//    public void setTimeStr(String timeStr) {
//        editor.putString("timeStr", timeStr);
//        editor.commit();
//    }
}

