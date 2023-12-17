package com.example.bookstore.app_util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.bookstore.App;
import com.example.bookstore.model.UserInfo;
import com.google.gson.Gson;

public class PreferenceUtils {

    private static final String USER_INFO = "USER_INFO";

    private static SharedPreferences preferences;

    public static synchronized void init() {
        preferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
    }

    //lưu lại user info từ object convert sang json string nên sẽ lưu được trong shared preference (shared preference không có kiểu lưu object, nên phải convert object sang string và lưu lại)
    public static void saveUserInfo(UserInfo userInfo) {
        //nếu object không null thì convert object sang string và lưu lại
        if (userInfo != null) {
            preferences.edit().putString(USER_INFO, new Gson().toJson(userInfo)).apply();
        } else {
            //ngược lại object null thì lưu string trống
            preferences.edit().putString(USER_INFO, "").apply();
        }
    }

    //lấy user infor từ shared preference
    public static UserInfo getUserInfo() {
        //lấy string json từ shared preference
        String userJson = preferences.getString(USER_INFO, "");
        //convert json string sang object userinfo
        if (!AppUtils.isEmpty(userJson)) return new Gson().fromJson(userJson, UserInfo.class);
        else return null;
    }

    //hàm kiểm tra app đã login chưa? true: đã login, false: chưa login
    public static boolean isLogin(){
        //kiểm tra nếu object user info == null trả về false (chưa login), nếu userinfo != null trả về true (đã login)
        return getUserInfo() != null;
    }

}
