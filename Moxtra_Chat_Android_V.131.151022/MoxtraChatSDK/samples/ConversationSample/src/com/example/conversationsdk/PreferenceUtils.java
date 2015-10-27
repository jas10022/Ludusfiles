package com.example.conversationsdk;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jeffery on 6/9/15.
 */
public class PreferenceUtils {
    private static final String PREFERENCE_NAME = "MOXTRA_WEIBO_LOGIN";

    private static PreferenceUtils preferenceUtil;

    private SharedPreferences sp;

    private SharedPreferences.Editor ed;

    private PreferenceUtils(Context context) {
        init(context);
    }

    public void init(Context context) {
        if (sp == null || ed == null) {
            try {
                sp = context.getSharedPreferences(PREFERENCE_NAME, 0);
                ed = sp.edit();
            } catch (Exception e) {
            }
        }
    }

    public static PreferenceUtils getInstance(Context context) {
        if (preferenceUtil == null) {
            preferenceUtil = new PreferenceUtils(context);
        }
        return preferenceUtil;
    }

    public void saveLong(String key, long l) {
        ed.putLong(key, l);
        ed.commit();
    }

    public long getLong(String key, long defaultlong) {
        return sp.getLong(key, defaultlong);
    }

    public void saveBoolean(String key, boolean value) {
        ed.putBoolean(key, value);
        ed.commit();
    }

    public boolean getBoolean(String key, boolean defaultboolean) {
        return sp.getBoolean(key, defaultboolean);
    }

    public void saveInt(String key, int value) {
        if (ed != null) {
            ed.putInt(key, value);
            ed.commit();
        }
    }

    public int getInt(String key, int defaultInt) {
        return sp.getInt(key, defaultInt);
    }

    public String getString(String key, String defaultInt) {
        return sp.getString(key, defaultInt);
    }

    public String getString(Context context, String key, String defaultValue) {
        if (sp == null || ed == null) {
            sp = context.getSharedPreferences(PREFERENCE_NAME, 0);
            ed = sp.edit();
        }
        if (sp != null) {
            return sp.getString(key, defaultValue);
        }
        return defaultValue;
    }

    public void saveString(String key, String value) {
        ed.putString(key, value);
        ed.commit();
    }

    public void remove(String key) {
        ed.remove(key);
        ed.commit();
    }

    public void destroy() {
        sp = null;
        ed = null;
        preferenceUtil = null;
    }
}
