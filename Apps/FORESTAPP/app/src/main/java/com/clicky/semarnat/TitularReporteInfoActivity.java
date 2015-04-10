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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 *
 * Created by Clicky on 4/5/15.
 *
 */
public class TitularReporteInfoActivity extends ActionBarActivity{

    private static String EXTRA_REPORTEID = "reporteId";
    private static String EXTRA_DOCUMENTOID = "docId";

    private SimpleDateFormat format;

    private TextView txtAgente, txtReporte, txtFechaReporte, txtTipo, txtFolioAut, txtFechaExp,
            txtFechaVen, txtCatNombre, txtCatDireccion, txtTransportistaNombre, txtMarca, txtModelo,
            txtPlacas, txtMateriaTipo, txtDesc, txtCant, txtVol, txtUnidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titular_reporte_info);

        String docId = getIntent().getExtras().getString(EXTRA_DOCUMENTOID);
        String reporteId = getIntent().getExtras().getString(EXTRA_REPORTEID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtAgente = (TextView)findViewById(R.id.txt_profepa_nombre);
        txtFechaReporte = (TextView)findViewById(R.id.txt_fecha_reporte);
        txtReporte = (TextView)findViewById(R.id.txt_reporte);
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

        format = new SimpleDateFormat("dd-mm-yyyy", Locale.US);

        findFolio(reporteId, docId);
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

    private void findFolio(String reporte, String documento){
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Inspecciones");
        query1.include("Inspector");
        query1.whereEqualTo("objectId",reporte);
        query1.fromLocalDatastore();
        query1.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject reporte, ParseException e) {
                if (e == null) {
                    if (reporte != null) {
                        getSupportActionBar().setTitle(reporte.getString("Reporte"));

                        txtAgente.setText(reporte.getParseObject("Inspector").getString("Nombre"));
                        txtFechaReporte.setText(format.format(reporte.getCreatedAt()));
                        txtReporte.setText(reporte.getString("Detalles"));
                    }
                } else {
                    // There was an error.
                    Log.i("ResultFragment", "findFolio: Error finding document: " + e.getMessage());
                }
            }
        });
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
                        txtTipo.setText(doc.getTipo());
                        txtFolioAut.setText(doc.getFolioAuto());
                        txtFechaExp.setText(format.format(doc.getFechaExp()));
                        txtFechaVen.setText(format.format(doc.getFechaVen()));
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
