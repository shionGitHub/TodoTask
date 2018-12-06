package com.yishion.minimal.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Objects;

public class SharedPreferencesUtils {

    public static final String THEME_DARK = "com.yishion.minimal.dark";//APP暗色的主题
    public static final String THEME_LIGHT = "com.yishion.minimal.light";//APP亮色的主题

    public static final String THEME_SAVE = "THEME_SAVE";


    private static String getCurrentTheme(Context ctx) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sharedPreferences.getString(THEME_SAVE, THEME_LIGHT);
    }

    public static void setCurrentTheme(Context ctx, String theme) {
        if (Objects.equals(theme, THEME_LIGHT)
                || Objects.equals(theme, THEME_DARK)) {
            SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(ctx);
            SharedPreferences.Editor editor = shared.edit();
            editor.putString(THEME_SAVE, theme);
            editor.apply();
        }
    }

    public static boolean isThemeLight(Context ctx) {
        return Objects.equals(getCurrentTheme(ctx), THEME_LIGHT);
    }

    public static final String DATA_CHANGED_APP = "data_changed_app";//app数据是否发生改变

    public static void saveDataChange(Context ctx, boolean isChange) {

        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean(DATA_CHANGED_APP, isChange);
        editor.apply();
    }

    public static boolean isDataChanged(Context ctx) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sharedPreferences.getBoolean(DATA_CHANGED_APP, false);
    }


    //Activity是否需要重新创建
    public static final String ACTIVITY_RECREATE = "com.yishion.recreate";


    public static void reCreate(Context ctx, boolean isChange) {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean(ACTIVITY_RECREATE, isChange);
        editor.apply();
    }

    public static boolean isReCreate(Context ctx) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sharedPreferences.getBoolean(ACTIVITY_RECREATE, false);
    }


}
