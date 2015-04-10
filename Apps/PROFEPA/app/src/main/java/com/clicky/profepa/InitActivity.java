package com.clicky.profepa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.clicky.profepa.data.PROFEPAPreferences;
import com.parse.ParseUser;

/**
 *
 * Created by Clicky on 3/22/15.
 *
 */
public class InitActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PROFEPAPreferences prefs = new PROFEPAPreferences(this);
        Intent i;
        if(ParseUser.getCurrentUser() != null){
            if(prefs.getFirst())
                i = new Intent(InitActivity.this, UpdateActivity.class);
            else
                i = new Intent(InitActivity.this, MainActivity.class);
        }else{
            i = new Intent(InitActivity.this, LoginActivity.class);
        }
        startActivity(i);
        finish();
        overridePendingTransition(0, 0);
    }

}
