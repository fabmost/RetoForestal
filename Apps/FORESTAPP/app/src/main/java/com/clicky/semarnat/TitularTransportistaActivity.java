package com.clicky.semarnat;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 *
 * Created by Clicky on 4/6/15.
 *
 */
public class TitularTransportistaActivity extends ActionBarActivity{

    private static String EXTRA_TRANSPORTISTA = "transportista";

    private TextView txtPropietario, txtMedio, txtTipo, txtMarca, txtModelo, txtCapacidad, txtPlacas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titular_transportista);

        String transportista = getIntent().getExtras().getString(EXTRA_TRANSPORTISTA);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtPropietario = (TextView)findViewById(R.id.txt_propietario);
        txtMedio = (TextView)findViewById(R.id.txt_medio);
        txtTipo = (TextView)findViewById(R.id.txt_tipo);
        txtMarca = (TextView)findViewById(R.id.txt_marca);
        txtModelo = (TextView)findViewById(R.id.txt_modelo);
        txtCapacidad = (TextView)findViewById(R.id.txt_capacidad);
        txtPlacas = (TextView)findViewById(R.id.txt_placas);

        findTransportista(transportista);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

    private void findTransportista(String transportista){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Transportistas");
        query.whereEqualTo("objectId",transportista);
        query.fromLocalDatastore();
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject transportistas, ParseException e) {
                if (e == null) {
                    if (transportistas != null) {
                        getSupportActionBar().setTitle(transportistas.getString("Chofer"));

                        txtPropietario.setText(transportistas.getString("Propietario"));
                        txtMedio.setText(transportistas.getString("Medio"));
                        txtTipo.setText(transportistas.getString("Tipo"));
                        txtMarca.setText(transportistas.getString("Marca"));
                        txtModelo.setText(transportistas.getString("Modelo"));
                        txtCapacidad.setText(transportistas.getString("Capacidad"));
                        txtPlacas.setText(transportistas.getString("Placas"));

                    }
                } else {
                    Log.i("TodoListActivity", "loadTransportistas: Error finding pinned todos: " + e.getMessage());
                }
            }
        });
    }
}
