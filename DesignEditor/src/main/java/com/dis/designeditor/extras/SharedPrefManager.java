package com.dis.designeditor.extras;

import android.content.Context;
import android.content.SharedPreferences;
import com.dis.designeditor.api.RetrofitClient;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = RetrofitClient.AppName;
    private static SharedPrefManager mInstance;
    private Context mCtx;
    private static final String TAG_TOKEN = "tagtoken";

    private SharedPrefManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public static synchronized SharedPrefManager getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }


    public int userExecutiveCdID() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("Executive_Cd", 0);
    }
    public String userExecutiveName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("ExecutiveName", "0");
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("User_Id", 0) != 0;
    }

    public int userID() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("User_Id", 0);
    }

    public String userName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("USerName", "0");
    }

    public String userDesignation() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("Designation", "0");
    }

    public String userMobile() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("Mobile", "0");
    }

    public int IsVerified() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("IsVerified", 0);
    }
    public boolean addIsVerified(int IsVerified) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("IsVerified", IsVerified);
        editor.apply();

        return sharedPreferences.getInt("IsVerified", 0) == IsVerified;
    }



}
