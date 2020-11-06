package com.example.registerlogin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Database {

    private final static String DATABASE_PHONE_NUMBER = "database_weight_key";
    private final static String DATABASE_PHONE_MESSAGE = "database_message_key";
    private final static String DATABASE_PRINT_MESSAGE = "database_print_message_key";
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;
    private static  Context mContext;
    private final static String PREF_NAME = "pref_DataBase"; // real File Name

    private static Database mInstance;
    public synchronized static Database getInstance(Context ctx) {
        mContext = ctx;
        if (mInstance == null) {
            mInstance = new Database();

            mSharedPreferences = ctx.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
            mEditor = mSharedPreferences.edit();
        }

        return mInstance;
    }

    public void setPhoneNumber( String input){
        String key = DATABASE_PHONE_NUMBER;
        mEditor.putString(key, input);
        mEditor.commit();
    }

    public String getPhoneNumber(){
        String key = DATABASE_PHONE_NUMBER;
        return mSharedPreferences.getString(key, "0");
    }

    public void setMessage(int index, String input){
        String key = DATABASE_PHONE_MESSAGE;
        mEditor.putString(key+index, input);
        mEditor.commit();
    }

    public String getMessage(int index){
        String key = DATABASE_PHONE_MESSAGE;
        return mSharedPreferences.getString(key+index, "0");
    }

    public void setPrintMessage( String input){
        String key = DATABASE_PRINT_MESSAGE;
        mEditor.putString(key, input);
        mEditor.commit();
    }

    public String getPrintMessage(){
        String key = DATABASE_PRINT_MESSAGE;
        return mSharedPreferences.getString(key, "0");
    }



}
