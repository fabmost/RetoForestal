package com.clicky.profepa.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 *
 * Created by Clicky on 4/3/15.
 *
 */
public class PROFEPAPreferences {

    private static final String PROFEPAPREFERENCES = "PROFEPAPrefs" ;
    private SharedPreferences preferences;

    public PROFEPAPreferences(Context context){
        preferences = context.getSharedPreferences(PROFEPAPREFERENCES, Context.MODE_PRIVATE);
    }

    public boolean getFirst(){
        return preferences.getBoolean("isFirst", true);
    }

    public String getLastUpdateDoc(){
        return preferences.getString("lastUpdateDoc", "");
    }

    public String getLastUpdateMat(){
        return preferences.getString("lastUpdateMat", "");
    }

    public String getInspector(){
        return preferences.getString("inspectorId", "");
    }

    public void setIsFirst(boolean isFirst){
        Editor editor = preferences.edit();
        editor.putBoolean("isFirst", isFirst);
        editor.commit();
    }

    public void setLastUpdateDocs(String lastUpdate){
        Editor editor = preferences.edit();
        editor.putString("lastUpdateDoc", lastUpdate);
        editor.commit();
    }

    public void setLastUpdateMat(String lastUpdate){
        Editor editor = preferences.edit();
        editor.putString("lastUpdateMat", lastUpdate);
        editor.commit();
    }

    public void setInspectorId(String inspectorId){
        Editor editor = preferences.edit();
        editor.putString("inspectorId", inspectorId);
        editor.commit();
    }

}
