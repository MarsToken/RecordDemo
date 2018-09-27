package com.example.administrator.recorddemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * <p>
 * Title: SharedPreferencesUtil.java
 * </p>
 * <p>
 * Description: TODO(SharedPreferences数据保存)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: FLJY Teachnology
 * </p>
 * <p>
 * CreateTime: 2015年5月13日 上午10:30:46自己修改成自己的
 * </p>
 *
 * @author Jeason
 * @version %A%, %B%
 * @CheckItem 自己填写
 * @since JDK1.6
 */
public class SharedPreferencesUtil {
    /**
     * 是否登录（0：未登录 1：帐号密码登录 2：第三方登录）
     */
    public static final String S_IS_THIRD_LOGIN = "s_is_login";
    /**
     * SharedPreferences保存是否第一次启动
     */
    public static final String S_KEY_ISFIRST = "APP_ISFIRST_VERMSG";
    /**
     * 用于验证登录权限
     */
    public static final String AUTH_KEY = "auth_key";
    /**
     * 用户类型
     */
    public static final String USER_TYPE = "user_type";
    /**
     * 登录类型
     */
    public static final String LOGIN_TYPE = "login_type";
    /**
     * 登录用户昵称
     */
    public static final String S_USER_NICK_NAME = "s_user_nick_name";
    /**
     * 登录手机号
     */
    public static final String S_USER_PHONE_NUM = "s_user_phone_num";
    /**
     * 登录密码
     */
    public static final String S_PASS_WORD = "s_pass_word";
    /***
     * 登录用户id
     */
    public static final String S_USER_ID = "s_user_id";

    /**
     * 登录同步云IM
     */
    public static final String S_USER_IM_TOKEN = "s_im_token";
    /**
     * 登录用户头像
     */
    public static final String S_USER_PIC_URL = "s_user_pic_url";
    /**
     * 登录用户头像缩略图
     */
    public static final String S_USER_PIC_SMALLURL = "s_user_pic_smallurl";
    /**
     * 登录用户性别
     */
    public static final String S_USER_GENDER = "s_user_gender";
    /**
     * 登录用户学号ID
     */
    public static final String S_USER_ISSNSLOGIN = "s_user_issnslogin";
    /**
     * 保存省的 json数据
     */
    public static final String S_USER_PRIVONCE_KEY_JSON = "s_user_privonce_key_json";
    /**
     * 保存服务器时间(针对码粒这一块)
     */
    public static final String S_USER_UPDATE_SERVERTIME = "s_user_update_servertime";
    /**
     * 设置webview 字体
     */
    public static final String STRING_SET_TEXT_SIZE = "set_text_size";

    /**
     * SharedPreferences保存同步学
     */
    public static final String SHARED_KEY_HOMEWORK_DESCRIB = "HOMEWORK_DESCRIB";
    public static final String S_SYNC_BOOK_GRADE_ID = "SYNC_BOOK_GRADE_ID";
    public static final String S_SYNC_BOOK_TITLE_NAME = "SYNC_BOOK_TITLE_NAME";
    public static final String S_SYNC_BOOK_ID = "SYNC_BOOK_ID";
    public static final String S_SYNC_BOOK_PIC = "SYNC_BOOK_PIC";
    public static final String S_SYNC_BOOK_PIC_SIZE = "SYNC_BOOK_PIC_SIZE";
    public static final String S_SYNC_BOOK_SUBTECT_ID = "SYNC_BOOK_SUBTECT_ID";
    public static final String S_SYNC_BOOK_SUBTECT_NAME = "SYNC_BOOK_SUBTECT_NAME";
    /**
     * 保存书架信息
     */
    public static final String S_SYNC_BOOKLIST_PUBLISHER_ID = "SYNC_BOOKLIST_PUBLISHER_ID";
    public static final String S_SYNC_BOOKLIST_PUBLISHER_NAME = "SYNC_BOOKLIST_PUBLISHER_NAME";
    /**
     * 书籍类型
     */
    public static final String S_SYNC_BOOK_TYPE = "SYNC_BOOK_TYPE";

    public static final String S_START_UP_IMG_TOP = "s_start_up_img_top";
    public static final String S_START_UP_IMG_BOTTOM = "s_start_up_img_bottom";
    public static final String S_LAST_GET_SUBJECT_TIME = "s_last_get_subject_time";

    /**
     * Description: SharedPreferences保存string
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setSetting(Context context, String key, String value) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        // editor 获取
        Editor editor = sp.edit();
        // 设置键值对
        editor.putString(key, value);
        // 递交保存
        editor.commit();
    }

    /**
     * Description: SharedPreferences保存string
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setSetting(Context context, String key, int value) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        // editor 获取
        Editor editor = sp.edit();
        // 设置键值对
        editor.putInt(key, value);
        // 递交保存
        editor.commit();
    }

    /**
     * Description: SharedPreferences保存boolean
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setSetting(Context context, String key, boolean value) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        // editor 获取
        Editor editor = sp.edit();
        // 设置键值对
        editor.putBoolean(key, value);
        // 递交保存
        editor.commit();
    }

    /**
     * Description: SharedPreferences获取string
     *
     * @param context
     * @param key
     * @return
     */
    public static String getSetting(Context context, String key) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        // 通过键获取值
        return sp.getString(key, null);
    }

    /**
     * Description: SharedPreferences获取string 带有默认值
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getSetting(Context context, String key, String defaultValue) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        // 通过键获取值
        return sp.getString(key, defaultValue);
    }

    /**
     * Description: SharedPreferences获取boolean 带有默认值
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBooleanSetting(Context context, String key, boolean defaultValue) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        // 通过键获取值
        return sp.getBoolean(key, defaultValue);

    }

    /**
     * Description: SharedPreferences获取boolean 带有默认值
     *
     * @param context
     * @param key
     * @return
     */
    public static int getIntSetting(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        // 通过键获取值
        return sp.getInt(key, 0);

    }

    /**
     * Description: SharedPreferences移除
     *
     * @param context
     * @param key
     */
    public static void removeSetting(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        // 删除键值
        editor.remove(key);
        editor.commit();
    }

    /**
     * Description: SharedPreferences清空
     *
     * @param context
     */
    public static void clearSetting(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        // 删除键值
        editor.clear();
        editor.commit();
    }


    public static SharedPreferences getLoginSpf(Context context){
        SharedPreferences spf = context.getSharedPreferences("isloginState", Context.MODE_PRIVATE);
        return spf;
    }

    /**
     * sp 键 ："isloginState"
     */
    public static void setLoginKey(Context context, String key, String value) {

        SharedPreferences spf = context.getSharedPreferences("isloginState", Context.MODE_PRIVATE);
        Editor editor = spf.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getLoginKey(Context context, String key, String defaultValue) {
        SharedPreferences spf = context.getSharedPreferences("isloginState", Context.MODE_PRIVATE);
        return spf.getString(key,defaultValue);

    }
}
