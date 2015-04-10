package com.clicky.semarnat.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * Created by Clicky on 4/3/15.
 *
 */
public class SEMARNATPreferences {

    private static final String SEMARNATPREFERENCES = "SEMARNATPrefs" ;
    private SharedPreferences preferences;

    public SEMARNATPreferences(Context context){
        preferences = context.getSharedPreferences(SEMARNATPREFERENCES, Context.MODE_PRIVATE);
    }

    public boolean getFirst(){
        return preferences.getBoolean("isFirst", true);
    }

    public String lastUpdate(){
        return preferences.getString("lastUpdate", "");
    }

    public String getUser(){
        return preferences.getString("userId", "");
    }

    public String getRol(){
        return preferences.getString("rol", "");
    }

    public void setIsFirst(boolean isFirst){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirst", isFirst);
        editor.commit();
    }

    public void setLastUpdate(String lastUpdate){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lastUpdate", lastUpdate);
        editor.commit();
    }

    public void setUserId(String userId){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userId", userId);
        editor.commit();
    }

    public void setRol(String rol){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("rol", rol);
        editor.commit();
    }

}
