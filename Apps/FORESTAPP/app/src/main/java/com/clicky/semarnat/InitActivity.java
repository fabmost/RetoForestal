package com.clicky.semarnat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.clicky.semarnat.data.SEMARNATPreferences;
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

        SEMARNATPreferences prefs = new SEMARNATPreferences(this);
        Intent i = null;
        if(ParseUser.getCurrentUser() != null){
            if(prefs.getFirst())
                i = new Intent(InitActivity.this, UpdateActivity.class);
            else {
                String rol = prefs.getRol();
                switch (rol) {
                    case "Titular":
                        i = new Intent(InitActivity.this, TitularMainActivity.class);
                        break;
                    case "Transportista":
                        i = new Intent(InitActivity.this, TransportistaMainActivity.class);
                        break;
                    case "CAT":
                        i = new Intent(InitActivity.this, CATMainActivity.class);
                        break;
                }
            }
        }else{
            i = new Intent(InitActivity.this, LoginActivity.class);
        }
        startActivity(i);
        finish();
        overridePendingTransition(0, 0);
    }

}
