package com.clicky.semarnat;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.clicky.semarnat.data.Documentos;
import com.clicky.semarnat.data.MateriaPrima;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 *
 * Created by Clicky on 4/5/15.
 *
 */
public class TitularDetallesActivity extends ActionBarActivity {

    private static String EXTRA_DOCUMENTOID = "docId";

    private TextView txtTipo, txtFolioAut, txtFechaExp,
            txtFechaVen, txtCatNombre, txtCatDireccion, txtTransportistaNombre, txtMarca, txtModelo,
            txtPlacas, txtMateriaTipo, txtDesc, txtCant, txtVol, txtUnidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titular_detalles);

        String docId = getIntent().getExtras().getString(EXTRA_DOCUMENTOID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTipo = (TextView)findViewById(R.id.txt_tipo);
        txtFolioAut = (TextView)findViewById(R.id.txt_folio_aut);
        txtFechaExp = (TextView)findViewById(R.id.txt_fecha_exp);
        txtFechaVen = (TextView)findViewById(R.id.txt_fecha_ven);
        txtCatNombre = (TextView)findViewById(R.id.txt_cat_nombre);
        txtCatDireccion = (TextView)findViewById(R.id.txt_cat_domicilio);
        txtTransportistaNombre = (TextView)findViewById(R.id.txt_transportista_nombre);
        txtMarca = (TextView)findViewById(R.id.txt_marca);
        txtModelo = (TextView)findViewById(R.id.txt_modelo);
        txtPlacas = (TextView)findViewById(R.id.txt_placas);
        txtMateriaTipo = (TextView)findViewById(R.id.txt_materia_tipo);
        txtDesc = (TextView)findViewById(R.id.txt_desc);
        txtCant = (TextView)findViewById(R.id.txt_cant);
        txtVol = (TextView)findViewById(R.id.txt_volumen);
        txtUnidad = (TextView)findViewById(R.id.txt_unidad);

        findFolio(docId);
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

    private void findFolio(String documento){
        ParseQuery<Documentos> query = Documentos.getQuery();
        query.include("Titular");
        query.include("Transportista");
        query.include("CAT");
        query.whereEqualTo("objectId", documento);
        query.fromLocalDatastore();
        query.getFirstInBackground(new GetCallback<Documentos>() {
            public void done(Documentos doc, ParseException e) {
                if (e == null) {
                    if(doc != null) {
                        // Results were successfully found from the local datastore.
                        getSupportActionBar().setTitle(doc.getFolioProg().toString());

                        txtTipo.setText(doc.getTipo());
                        txtFolioAut.setText(doc.getFolioAuto());
                        txtFechaExp.setText(doc.getFechaExp().toString());
                        txtFechaVen.setText(doc.getFechaVen().toString());
                        txtCatNombre.setText(doc.getCAT().getString("Nombre"));
                        txtCatDireccion.setText(doc.getCAT().getString("Domicilio"));
                        txtTransportistaNombre.setText(doc.getTransportista().getString("Chofer"));
                        txtMarca.setText(doc.getTransportista().getString("Marca"));
                        txtModelo.setText(doc.getTransportista().getString("Modelo"));
                        txtPlacas.setText(doc.getTransportista().getString("Placas"));
                    }
                } else {
                    // There was an error.
                    Log.i("ResultFragment", "findFolio: Error finding document: " + e.getMessage());
                }
            }
        });

        ParseQuery<MateriaPrima> query2 = MateriaPrima.getQuery();
        query2.whereEqualTo("Documentos", ParseObject.createWithoutData("Documentos", documento));
        query2.fromLocalDatastore();
        query2.findInBackground(new FindCallback<MateriaPrima>() {
            public void done(final List<MateriaPrima> scoreList, ParseException e) {
                if (e == null) {
                    // Results were successfully found from the local datastore.
                    if(!scoreList.isEmpty()) {
                        MateriaPrima materia = scoreList.get(0);
                        txtMateriaTipo.setText(materia.getTipo());
                        txtDesc.setText(materia.getDescripcion());
                        txtCant.setText(materia.getCantidad().toString());
                        txtVol.setText(materia.getVolumen());
                        txtUnidad.setText(materia.getUnidad());
                    }
                } else {
                    // There was an error.
                    Log.i("ResultFragment", "findFolio: Error finding document: " + e.getMessage());
                }
            }
        });
    }

}
