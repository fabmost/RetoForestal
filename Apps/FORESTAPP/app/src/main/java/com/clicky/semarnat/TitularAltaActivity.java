package com.clicky.semarnat;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.clicky.semarnat.data.Documentos;
import com.clicky.semarnat.data.MateriaPrima;
import com.clicky.semarnat.data.SEMARNATPreferences;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by Clicky on 4/5/15.
 *
 */
public class TitularAltaActivity extends ActionBarActivity{

    private Spinner spinTipo, spinTransportistas, spinCAT;
    private MaterialEditText editFolio, editMateria, editDescripcion, editCantidad, editVolumen, editUnidad;
    private List<String> transportistasList, catList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titular_alta);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinTipo = (Spinner)findViewById(R.id.spin_tipo);
        spinTransportistas = (Spinner)findViewById(R.id.spin_transportista);
        spinCAT = (Spinner)findViewById(R.id.spin_destino);

        editFolio = (MaterialEditText)findViewById(R.id.edit_folio);
        editMateria = (MaterialEditText)findViewById(R.id.edit_producto);
        editDescripcion = (MaterialEditText)findViewById(R.id.edit_descripcion);
        editCantidad = (MaterialEditText)findViewById(R.id.edit_cantidad);
        editVolumen = (MaterialEditText)findViewById(R.id.edit_volumen);
        editUnidad = (MaterialEditText)findViewById(R.id.edit_unidad);

        getTransportistas();
        getCAT();

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
                sendAlta();
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

    private void getTransportistas(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Transportistas");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> transportistas, ParseException e) {
                if(e == null) {
                    transportistasList = new ArrayList<>();
                    List<String> tList = new ArrayList<>();
                    for(ParseObject obj : transportistas){
                        transportistasList.add(obj.getObjectId());
                        tList.add(obj.getString("Chofer"));
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(TitularAltaActivity.this, android.R.layout.simple_spinner_item, tList);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinTransportistas.setAdapter(spinnerArrayAdapter);
                }else{
                    Log.i("TodoListActivity", "getTransportistas: Error finding pinned todos: " + e.getMessage());
                }
            }
        });
    }

    private void getCAT(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CAT");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> transportistas, ParseException e) {
                if(e == null) {
                    catList = new ArrayList<>();
                    List<String> cList = new ArrayList<>();
                    for(ParseObject obj : transportistas){
                        catList.add(obj.getObjectId());
                        cList.add(obj.getString("Nombre"));
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(TitularAltaActivity.this, android.R.layout.simple_spinner_item, cList);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinCAT.setAdapter(spinnerArrayAdapter);
                }else{
                    Log.i("TodoListActivity", "getTransportistas: Error finding pinned todos: " + e.getMessage());
                }
            }
        });
    }

    private void sendAlta(){
        SEMARNATPreferences pref = new SEMARNATPreferences(this);
        Date fecha = new Date(System.currentTimeMillis());
        Date fechaVen = new Date(System.currentTimeMillis());
        fechaVen.setDate(fecha.getDay()+5);

        Documentos documento = new Documentos();
        documento.put("Tipo", (spinTipo.getSelectedItem().toString().split(" "))[0]);
        documento.put("Folio_prog", Double.parseDouble(editFolio.getText().toString()));
        documento.put("Folio_auto","350");
        documento.put("Fecha_exp",fecha);
        documento.put("Fecha_ven",fechaVen);
        documento.put("Titular",ParseObject.createWithoutData("Titular", pref.getUser()));
        documento.put("Transportista",ParseObject.createWithoutData("Transportistas", transportistasList.get(spinTransportistas.getSelectedItemPosition())));
        documento.put("CAT",ParseObject.createWithoutData("CAT", catList.get(spinCAT.getSelectedItemPosition())));
        documento.saveEventually();

        MateriaPrima materia = new MateriaPrima();
        materia.put("Tipo",editMateria.getText().toString());
        materia.put("numer_cantidad",Double.parseDouble(editCantidad.getText().toString()));
        materia.put("descripcion",editDescripcion.getText().toString());
        materia.put("volumen_peso_amp",editVolumen.getText().toString());
        materia.put("unidad_medida",editUnidad.getText().toString());
        materia.put("saldo_disp",300);
        materia.put("saldo_rest",250);
        materia.put("Documentos",documento);
        materia.saveEventually();

        documento.pinInBackground();
        materia.pinInBackground();

        Toast.makeText(this, R.string.toast_sent, Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

}
