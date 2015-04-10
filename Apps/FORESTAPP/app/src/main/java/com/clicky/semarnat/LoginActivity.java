package com.clicky.semarnat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.clicky.semarnat.data.SEMARNATPreferences;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 *
 * Created by Clicky on 3/22/15.
 *
 */
public class LoginActivity extends ActionBarActivity {

    private SEMARNATPreferences prefs;
    private MaterialEditText editUser, editPassword;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUser = (MaterialEditText)findViewById(R.id.edit_user);
        editPassword = (MaterialEditText)findViewById(R.id.edit_password);
        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        TextView btnRecover = (TextView)findViewById(R.id.btn_recover);

        prefs = new SEMARNATPreferences(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.dialog_loading, R.style.AppTheme));
        dialog.setCancelable(false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });

        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMailDialog();
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
            public void done(final ParseUser user, ParseException e) {
                if (user != null) {
                    user.getParseObject("Rol").fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            findInspector(user, parseObject.getString("Nombre"));
                        }
                    });
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    if (dialog.isShowing())
                        dialog.dismiss();
                    Toast.makeText(LoginActivity.this, R.string.error_login, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findInspector(final ParseUser user, final String rol){
        ParseQuery<ParseObject> query = null;
        switch (rol) {
            case "Titular de aprovechamiento forestal":
                query = ParseQuery.getQuery("Titular");
                prefs.setRol("Titular");
                break;
            case "Transportista":
                query = ParseQuery.getQuery("Transportistas");
                prefs.setRol("Transportista");
                break;
            case "CAT":
                query = ParseQuery.getQuery("CAT");
                prefs.setRol("CAT");
                break;
        }
        if(query != null) {
            query.whereEqualTo("Usuario", user);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(final ParseObject usuario, ParseException e) {
                    if (e == null && usuario != null) {
                        if(rol.equals("Titular de aprovechamiento forestal") || rol.equals("Transportista")){
                            ParsePush.subscribeInBackground("C"+usuario.getObjectId(),new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (dialog.isShowing())
                                        dialog.dismiss();
                                    prefs.setUserId(usuario.getObjectId());
                                    toActivity();
                                    if(e != null)
                                        Log.e("ERROR",e.getMessage());
                                }
                            });
                        }else {
                            if (dialog.isShowing())
                                dialog.dismiss();
                            prefs.setUserId(usuario.getObjectId());
                            toActivity();
                        }
                    }else if(e != null) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        if(rol.equals("Transportista")){
                            ParseUser.logOut();
                            Toast.makeText(LoginActivity.this, R.string.error_datos, Toast.LENGTH_SHORT).show();
                        }else if(rol.equals("Titular de aprovechamiento forestal") || rol.equals("CAT") ){
                            Intent i = new Intent(LoginActivity.this, AltaDatosActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }
            });
        }
    }

    private void showMailDialog(){
        Resources res = getResources();
        new MaterialDialog.Builder(this)
                .title(R.string.dialog_recover)
                .customView(R.layout.dialog_recover_password, false)
                .titleColor(res.getColor(R.color.accent))
                .positiveColor(res.getColor(R.color.accent))
                .positiveText(R.string.btn_ok)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        View v = dialog.getCustomView();
                        EditText edit = (EditText) v.findViewById(R.id.edit_mail);
                        resetPassword(edit.getText().toString());
                        super.onPositive(dialog);
                    }
                })
                .show();
    }

    private void resetPassword(String email){
        dialog.show();
        ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            public void done(ParseException e) {
                if(dialog.isShowing())
                    dialog.dismiss();
                if (e == null) {
                    // An email was successfully sent with reset instructions.
                    Toast.makeText(LoginActivity.this, R.string.toast_recover, Toast.LENGTH_LONG).show();
                } else {
                    // Something went wrong. Look at the ParseException to see what's up.
                    Toast.makeText(LoginActivity.this, R.string.toast_no_folio, Toast.LENGTH_SHORT).show();
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
