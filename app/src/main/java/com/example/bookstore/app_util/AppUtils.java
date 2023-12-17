package com.example.bookstore.app_util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


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

    public static String getNowDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }


    public static void setVisibility(boolean isVisible, View... view) {
        for (View v : view)
            if (v != null) v.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public static String formatMoneyLong(long value, String unit) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat format = new DecimalFormat("#,###.##", symbols);
        format.setGroupingUsed(true);
        return format.format(value) + (AppUtils.isEmpty(unit) ? "" : unit);
    }

    public static String formatMoneyDouble(double value, String unit) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat format = new DecimalFormat("#,###.##", symbols);
        format.setGroupingUsed(true);
        return format.format(value) + (AppUtils.isEmpty(unit) ? "" : unit);
    }

    public static <T> String formatMoney(T money, String unit) {
        if (money instanceof String) return formatMoney((String) money, unit, null);
        else if (money instanceof Integer) return formatMoneyLong((Integer) money, unit);
        else if (money instanceof Long) return formatMoneyLong((Long) money, unit);
        else if (money instanceof Float) return formatMoneyDouble((Float) money, unit);
        else if (money instanceof Double) return formatMoneyDouble((Double) money, unit);
        else if (money != null) return formatMoney(money.toString(), unit);
        else return null;
    }

    public static String formatMoney(String strMoney, String unit, String valueIfEmpty) {
        String moneyFormat;
        if (TextUtils.isEmpty(strMoney)) {
            return isEmpty(valueIfEmpty) ? "" : valueIfEmpty;
        } else {
            String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
            String strDefault = strMoney.replaceAll(replaceable, "");
            strDefault = strDefault.replaceAll("-", "");
            strDefault = strDefault.replace(".", "");
            int length = strDefault.length();
            if (length < 4) {
                moneyFormat = strDefault;
            } else if (length < 7) {
                moneyFormat = strDefault.substring(0, length - 3) + "."
                        + strDefault.substring(length - 3, length);
            } else if (length < 10) {
                moneyFormat = strDefault.substring(0, length - 6) + "."
                        + strDefault.substring(length - 6, length - 3) + "."
                        + strDefault.substring(length - 3, length);
            } else {
                moneyFormat = strDefault.substring(0, length - 9) + "."
                        + strDefault.substring(length - 9, length - 6) + "."
                        + strDefault.substring(length - 6, length - 3) + "."
                        + strDefault.substring(length - 3, length);
            }
            return moneyFormat + (AppUtils.isEmpty(unit) ? "" : unit);
        }
    }

    public static String formatMoney(long value) {
        return formatMoney(value, null);
    }

    public static String formatMoney(double value) {
        return formatMoney(value, null);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.findViewById(android.R.id.content).getWindowToken(), 0);
            View current = activity.getCurrentFocus();
            if (current != null) current.clearFocus();
        }
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        }
    }

}
