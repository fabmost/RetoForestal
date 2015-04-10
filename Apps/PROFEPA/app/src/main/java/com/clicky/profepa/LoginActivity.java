package com.clicky.profepa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.clicky.profepa.data.PROFEPAPreferences;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 *
 * Created by Clicky on 3/22/15.
 *
 */
public class LoginActivity extends ActionBarActivity {

    private PROFEPAPreferences prefs;
    private MaterialEditText editUser, editPassword;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUser = (MaterialEditText)findViewById(R.id.edit_user);
        editPassword = (MaterialEditText)findViewById(R.id.edit_password);
        Button btnLogin = (Button)findViewById(R.id.btnLogin);

        prefs = new PROFEPAPreferences(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.dialog_loading, R.style.AppTheme));
        dialog.setCancelable(false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });
    }

    private void verify(){
        if(editUser.getText().length() == 0 || editPassword.getText().length() == 0){
            Toast.makeText(this,R.string.error_fields,Toast.LENGTH_SHORT).show();
        }else{
            loginUser();
        }
    }

    private void loginUser(){
        dialog.show();
        ParseUser.logInInBackground(editUser.getText().toString(), editPassword.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    findInspector(user);
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    if(dialog.isShowing())
                        dialog.dismiss();
                    Toast.makeText(LoginActivity.this,R.string.error_login,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findInspector(ParseUser user){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Inspectores");
        query.whereEqualTo("Usuario", user);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject inspector, ParseException e) {
                if(dialog.isShowing())
                    dialog.dismiss();
                if (e == null && inspector != null) {
                    prefs.setInspectorId(inspector.getObjectId());
                    toActivity();
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void toActivity(){
        Intent i = new Intent(LoginActivity.this, UpdateActivity.class);
        startActivity(i);
        finish();
    }

}
