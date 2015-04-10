package com.clicky.profepa.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 *
 * Created by Clicky on 4/3/15.
 *
 */
public class Utils {

    public static boolean isConnected(Context mContext){
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return  ( (ni != null) && (ni.isConnected()) );
    }
}
