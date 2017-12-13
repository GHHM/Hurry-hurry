package android.org.Hurryhurry_restaurant_client.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Jaena Jang on 2017-12-11.
 */

public class SharedPreferencesUtil {

    // Shared preferences String
    public static final String RFID_ID = "rfid_id";
    public static final String RFID_IS_USING = "rfid_is_using";
    public static final String MEMBER_ID = "member_id";
    public static final String FOOD_NAME = "food_name";
    public static final String BT_RECV_TIME = "bt_recv_time";

    // put value
    public static synchronized void put(Context context, String key, String value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public static synchronized void put(Context context, String key, boolean value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }

    public static synchronized void put(Context context, String key, int value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(key, value);
        editor.commit();
    }

    public static synchronized void put(Context context, String key, long value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putLong(key, value);
        editor.commit();
    }

    // get value
    public static synchronized String getValue(Context context, String key, String dftValue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        try {
            return pref.getString(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }

    }

    public static synchronized int getValue(Context context, String key, int dftValue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        try {
            return pref.getInt(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }

    }

    public static synchronized boolean getValue(Context context,String key, boolean dftValue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        try {
            return pref.getBoolean(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public static synchronized long getValue(Context context,String key, long dftValue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        try {
            return pref.getLong(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public static synchronized void clear(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

}
