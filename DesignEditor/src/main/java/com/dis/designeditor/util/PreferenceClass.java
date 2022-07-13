package com.dis.designeditor.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceClass {
    public static final String PREFERENCE_NAME = "LanguageData";
    private final SharedPreferences sharedpreferences;

    public PreferenceClass(Context context) {
        sharedpreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public String getLang() {
        String count = sharedpreferences.getString("lang", "en");
        return count;
    }

    public void setLang(String count) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("lang", count);
        editor.commit();
    }
}
