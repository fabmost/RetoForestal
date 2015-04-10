package com.clicky.semarnat;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clicky.semarnat.data.SEMARNATPreferences;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 *
 * Created by Clicky on 4/9/15.
 *
 */
public class AltaDatosActivity extends ActionBarActivity{

    private MaterialEditText editNombre, editMail, editDomicilio, editCurp, editCodigoIden, editSIEM, editRFN, editCodigoAut;
    private SEMARNATPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_datos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = new SEMARNATPreferences(this);

        TextView txtUser = (TextView)findViewById(R.id.txt_user);
        TextView txtRol = (TextView)findViewById(R.id.txt_rol);
        LinearLayout layoutCAT = (LinearLayout)findViewById(R.id.layout_cat);
        LinearLayout layoutTitular = (LinearLayout)findViewById(R.id.layout_titular);

        editNombre = (MaterialEditText)findViewById(R.id.edit_nombre);
        editMail = (MaterialEditText)findViewById(R.id.edit_mail);
        editDomicilio = (MaterialEditText)findViewById(R.id.edit_domicilio);
        editCurp = (MaterialEditText)findViewById(R.id.edit_curp);
        editCodigoIden = (MaterialEditText)findViewById(R.id.edit_codigo_iden);
        editSIEM = (MaterialEditText)findViewById(R.id.edit_siem);
        editRFN = (MaterialEditText)findViewById(R.id.edit_rfn);
        editCodigoAut = (MaterialEditText)findViewById(R.id.edit_codigo_aut);

        txtUser.setText(ParseUser.getCurrentUser().getUsername());
        txtRol.setText(prefs.getRol());

        if(prefs.getRol().equals("CAT")){
            layoutCAT.setVisibility(View.VISIBLE);
            layoutTitular.setVisibility(View.GONE);
        }else if(prefs.getRol().equals("Titular")){
            layoutCAT.setVisibility(View.GONE);
            layoutTitular.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cat_validar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_send:
                sendDatos();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendDatos(){
        ParseUser user = ParseUser.getCurrentUser();
        if(prefs.getRol().equals("CAT")){
            ParseObject cat = new ParseObject("CAT");
            cat.add("Nombre",editNombre.getText().toString());

            user.setEmail(editMail.getText().toString());

            cat.add("Domicilio",editDomicilio.getText().toString());
            cat.add("CURP",editCurp.getText().toString());
            cat.add("Codigo_iden",editCodigoIden.getText().toString());
            cat.add("codigo_autorizacion",editCodigoAut.getText().toString());
            cat.add("Usuario",user);
            cat.saveEventually();
            prefs.setUserId(cat.getObjectId());
        }else if(prefs.getRol().equals("Titular")){
            ParseObject titular = new ParseObject("Titular");
            titular.add("Nombre",editNombre.getText().toString());

            titular.add("Correo",editMail.getText().toString());
            user.setEmail(editMail.getText().toString());

            titular.add("Domicilio",editDomicilio.getText().toString());
            titular.add("CURP",editCurp.getText().toString());
            titular.add("SIEM",editSIEM.getText().toString());
            titular.add("RFN",editRFN.getText().toString());
            titular.add("Codigo_autorizacion",editCodigoAut.getText().toString());
            titular.add("Usuario",user);
            titular.saveEventually();
            prefs.setUserId(titular.getObjectId());
        }
        user.saveEventually();
    }

}
