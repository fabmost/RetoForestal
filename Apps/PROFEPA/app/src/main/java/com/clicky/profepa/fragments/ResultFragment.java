package com.clicky.profepa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.clicky.profepa.R;
import com.clicky.profepa.data.Documentos;
import com.clicky.profepa.data.MateriaPrima;
import com.clicky.profepa.data.PROFEPAPreferences;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 *
 * Created by Clicky on 3/22/15.
 *
 */
public class ResultFragment extends Fragment {

    private static String EXTRA_FOLIO = "folio";

    private TextView txtTipo, txtFolioAut, txtFechaExp, txtFechaVen, txtCatNombre, txtCatDireccion,
            txtTransportistaNombre, txtMarca, txtModelo, txtPlacas, txtMateriaTipo, txtDesc, txtCant,
            txtVol, txtUnidad;

    private String folio;

    public static ResultFragment newInstance(String folio){
        ResultFragment frag = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_FOLIO, folio);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_result, container, false);

        Toolbar toolbar = (Toolbar)v.findViewById(R.id.toolbar);
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);

        txtTipo = (TextView)v.findViewById(R.id.txt_tipo);
        txtFolioAut = (TextView)v.findViewById(R.id.txt_folio_aut);
        txtFechaExp = (TextView)v.findViewById(R.id.txt_fecha_exp);
        txtFechaVen = (TextView)v.findViewById(R.id.txt_fecha_ven);
        txtCatNombre = (TextView)v.findViewById(R.id.txt_cat_nombre);
        txtCatDireccion = (TextView)v.findViewById(R.id.txt_cat_domicilio);
        txtTransportistaNombre = (TextView)v.findViewById(R.id.txt_transportista_nombre);
        txtMarca = (TextView)v.findViewById(R.id.txt_marca);
        txtModelo = (TextView)v.findViewById(R.id.txt_modelo);
        txtPlacas = (TextView)v.findViewById(R.id.txt_placas);
        txtMateriaTipo = (TextView)v.findViewById(R.id.txt_materia_tipo);
        txtDesc = (TextView)v.findViewById(R.id.txt_desc);
        txtCant = (TextView)v.findViewById(R.id.txt_cant);
        txtVol = (TextView)v.findViewById(R.id.txt_volumen);
        txtUnidad = (TextView)v.findViewById(R.id.txt_unidad);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        decodeArguments();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findFolio();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_result, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.action_ok:
                sendReport();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void decodeArguments(){
        Bundle args = getArguments();
        if(args != null) {
            folio = args.getString(EXTRA_FOLIO);
        }
    }

    private void setUpBar(String title){
        ActionBarActivity activity = (ActionBarActivity)getActivity();
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle(title);
    }

    private void findFolio(){
        ParseQuery<Documentos> query = Documentos.getQuery();
        query.include("Transportista");
        query.include("CAT");
        query.whereEqualTo("objectId", folio);
        query.fromLocalDatastore();
        query.getFirstInBackground(new GetCallback<Documentos>() {
            public void done(Documentos doc, ParseException e) {
                if (e == null) {
                    if(doc != null) {
                        // Results were successfully found from the local datastore.
                        setUpBar(doc.getFolioProg().toString());

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
                    if(e.getCode() == 101)
                        Toast.makeText(getActivity(), R.string.error_result, Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
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

    private void sendReport(){
        PROFEPAPreferences prefs = new PROFEPAPreferences(getActivity());
        ParseObject inspeccion = new ParseObject("Inspecciones");
        inspeccion.put("Reporte", "Inspección");
        inspeccion.put("Detalles", "");
        inspeccion.put("Inspector", ParseObject.createWithoutData("Inspectores", prefs.getInspector()));
        inspeccion.put("Documentos", ParseObject.createWithoutData("Documentos", folio));
        inspeccion.saveEventually();

        Toast.makeText(getActivity(), R.string.toast_sent, Toast.LENGTH_LONG).show();
        getActivity().onBackPressed();
    }

}
