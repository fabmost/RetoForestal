package com.clicky.semarnat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
public class CATRegistroActivity extends ActionBarActivity implements View.OnClickListener{

    private static String EXTRA_DOCUMENTO = "docId";
    private static int VALIDAR_ACTIVITY = 100;

    private LinearLayout layoutSearch, layoutDatos;
    private EditText editFolio;
    private TextView txtTipo, txtFolioAut, txtFechaExp, txtFechaVen, txtTitularNombre, txtCatNombre,
            txtCatDireccion, txtTransportistaNombre, txtMarca, txtModelo, txtPlacas, txtMateriaTipo,
            txtDesc, txtCant, txtVol, txtUnidad;

    private String docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_registrar);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutSearch = (LinearLayout)findViewById(R.id.layout_search);
        layoutDatos = (LinearLayout)findViewById(R.id.layout_datos);

        txtTipo = (TextView)findViewById(R.id.txt_tipo);
        txtFolioAut = (TextView)findViewById(R.id.txt_folio_aut);
        txtFechaExp = (TextView)findViewById(R.id.txt_fecha_exp);
        txtFechaVen = (TextView)findViewById(R.id.txt_fecha_ven);
        txtTitularNombre = (TextView)findViewById(R.id.txt_titular_nombre);
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

        editFolio = (EditText)findViewById(R.id.edit_folio);
        Button btnSearch = (Button)findViewById(R.id.btnSearch);

        if(getIntent().getExtras() != null){
            layoutSearch.setVisibility(View.GONE);
            String docId = getIntent().getExtras().getString(EXTRA_DOCUMENTO);
            findFolio(docId,0);
        }

        btnSearch.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cat_registrar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_ok:
                validate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VALIDAR_ACTIVITY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                finish();
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

    @Override
    public void onClick(View v){
        findFolio(editFolio.getText().toString(),1);
    }

    private void findFolio(String folio, int tipo){
        ParseQuery<Documentos> query = Documentos.getQuery();
        query.include("Titular");
        query.include("Transportista");
        query.include("CAT");
        if(tipo == 0)
            query.whereEqualTo("objectId", folio);
        else if(tipo == 1)
            query.whereEqualTo("Folio_prog", folio);
        query.fromLocalDatastore();
        query.getFirstInBackground(new GetCallback<Documentos>() {
            public void done(Documentos doc, ParseException e) {
                if (e == null) {
                    if(doc != null) {
                        // Results were successfully found from the local datastore.
                        docId = doc.getObjectId();
                        SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy", Locale.US);

                        layoutDatos.setVisibility(View.VISIBLE);
                        txtTipo.setText(doc.getTipo());
                        txtFolioAut.setText(doc.getFolioAuto());
                        txtFechaExp.setText(format.format(doc.getFechaExp()));
                        txtFechaVen.setText(format.format(doc.getFechaVen()));
                        txtTitularNombre.setText(doc.getTitular().getString("Nombre"));
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
                    layoutDatos.setVisibility(View.GONE);
                    Toast.makeText(CATRegistroActivity.this,R.string.toast_no_results,Toast.LENGTH_SHORT).show();
                }
            }
        });

        ParseQuery<MateriaPrima> query2 = MateriaPrima.getQuery();
        query2.whereEqualTo("Documentos", ParseObject.createWithoutData("Documentos", folio));
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

    private void validate(){
        if(layoutDatos.getVisibility() == View.VISIBLE) {
            Intent i = new Intent(CATRegistroActivity.this, CATValidarActivity.class);
            i.putExtra(EXTRA_DOCUMENTO, docId);
            startActivityForResult(i, VALIDAR_ACTIVITY);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }else{
            Toast.makeText(this,R.string.toast_no_folio,Toast.LENGTH_SHORT).show();
        }
    }

}
