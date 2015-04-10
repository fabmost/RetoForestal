package com.clicky.semarnat.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;

import com.clicky.semarnat.LoginActivity;
import com.clicky.semarnat.data.SEMARNATPreferences;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;

import java.util.List;

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

    public static void logOut(ActionBarActivity mActivity){
        if(ParseUser.getCurrentUser() != null){
            SEMARNATPreferences pref = new SEMARNATPreferences(mActivity);
            pref.setIsFirst(true);
            ParseObject.unpinAllInBackground();
            List<String> channels = ParseInstallation.getCurrentInstallation().getList("channels");
            for(String channel : channels){
                ParsePush.unsubscribeInBackground(channel);
            }
            ParseUser.logOut();
            Intent i = new Intent(mActivity, LoginActivity.class);
            mActivity.startActivity(i);
            mActivity.finish();
        }
    }
}
