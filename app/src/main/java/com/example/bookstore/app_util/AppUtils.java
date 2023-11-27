package com.example.bookstore.app_util;

import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class AppUtils {
    private static Gson defaultGson;

    static {
        defaultGson = new Gson();
    }

    public static Gson gson() {
        if (defaultGson == null) defaultGson = new Gson();
        return defaultGson;
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || TextUtils.getTrimmedLength(str) == 0 || "null".equalsIgnoreCase(str.toString().trim());
    }

    public static Date getDate(String dateString, String format) {
        try {
            SimpleDateFormat outFmt = new SimpleDateFormat(format, Locale.US);
            return outFmt.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String formatDate(Date date, String toFormat) {
        if (date == null)
            return "";

        String formatDate = "";
        try {
            SimpleDateFormat outFmt = new SimpleDateFormat(toFormat, Locale.US);
            formatDate = outFmt.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return formatDate;
    }

    public static String formatDate(String strDate, String fromFormat, String toFormat) {
        if (strDate == null)
            return "";

        String formatDate = "";
        try {
            SimpleDateFormat inFmt = new SimpleDateFormat(fromFormat, Locale.US);
            SimpleDateFormat outFmt = new SimpleDateFormat(toFormat, Locale.US);
            Date date = inFmt.parse(strDate);
            formatDate = outFmt.format(Objects.requireNonNull(date));
        } catch (ParseException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return formatDate;
    }

    public static String formatDate(String strDate, SimpleDateFormat fromFormat, SimpleDateFormat toFormat) {
        if (strDate == null || AppUtils.isEmpty(strDate) || strDate.equals("0"))
            return "";

        String formatDate = "";
        try {
            formatDate = toFormat.format(Objects.requireNonNull(fromFormat.parse(strDate)));
        } catch (ParseException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return formatDate;
    }

    public static String getNowDateGMT() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(new Date());
    }


    public static void setVisibility(boolean isVisible, View... view) {
        for (View v : view)
            if (v != null) v.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}
