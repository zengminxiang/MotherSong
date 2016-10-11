package com.fanstech.mothersong.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *作者：胖胖祥
 *时间：2016/6/13 0013 下午 6:50
 *功能模块：工具类
 */
public class AllUtil {

    /**
     * 判断str 是否为null或者""
     *
     * @param str
     * @return
     */
    public static boolean isNotNull(String str) {
        if (str == null || "".equals(str))
            return false;
        else
            return true;

    }

    // 判断手机号是否正确
    public boolean checkPhone(String phone) {

        Pattern pattern = Pattern.compile("^(13|15|18)\\d{9}$");
        Matcher matcher = pattern.matcher(phone);

        if (matcher.matches()) {

            return true;

        }

        return false;
    }

    // float保留两位小数
    public float getFloatTwo(float f) {

        BigDecimal b = new BigDecimal(f);

        return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

    }

    //组合url
    public String CombinationUrl(String part, Map<String, String> params){

        // 处理乱码
        StringBuilder sb = new StringBuilder(part);

        if (params != null) {

            sb.append('?');
            for (Map.Entry<String, String> entry : params.entrySet()) {

                try {
                    sb.append(entry.getKey()).append('=')
                            .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                            .append('&');
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            sb.deleteCharAt(sb.length() - 1);

            Log.e("url", sb.toString());

        }

        return sb.toString();

    }

    // 金额验证
    public boolean isNumber(String str) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern
                .compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        java.util.regex.Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }


    //解析数字，总金额

    public int[] Number(String num) {

        String str = num;// 转化为字符串
        int[] intArray = new int[str.length()];// 新建一个数组用来保存num每一位的数字
        for (int i = 0; i < str.length(); i++) {

            // 遍历str将每一位数字添加如intArray
            Character ch = str.charAt(i);
            intArray[i] = Integer.parseInt(ch.toString());

        }

        return intArray;

    }



}
