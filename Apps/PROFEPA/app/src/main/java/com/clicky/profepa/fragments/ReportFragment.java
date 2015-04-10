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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clicky.profepa.R;
import com.clicky.profepa.data.Documentos;
import com.clicky.profepa.data.PROFEPAPreferences;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 *
 * Created by Clicky on 3/22/15.
 *
 */
public class ReportFragment extends Fragment {

    private static String EXTRA_FOLIO = "folio";

    private TextView txtFolio, txtTitular;
    private EditText editReport;
    private String folio;

    public static ReportFragment newInstance(String folio){
        ReportFragment frag = new ReportFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_FOLIO, folio);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report, container, false);

        Toolbar toolbar = (Toolbar)v.findViewById(R.id.toolbar);
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        setUpBar();

        txtFolio = (TextView)v.findViewById(R.id.txt_folio);
        txtTitular = (TextView)v.findViewById(R.id.txt_titular);
        editReport = (EditText)v.findViewById(R.id.edit_reporte);

        getFolio();

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        decodeArguments();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_reporte, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.action_send:
                if(validate()){
                    sendReport();
                }else{
                    Toast.makeText(getActivity(),R.string.error_fields,Toast.LENGTH_SHORT).show();
                }
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

    private boolean validate(){
        return !editReport.getText().toString().isEmpty();
    }

    private void setUpBar(){
        ActionBarActivity activity = (ActionBarActivity)getActivity();
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().setTitle(R.string.title_reportar);
    }

    private void getFolio(){
        ParseQuery<Documentos> query = Documentos.getQuery();
        query.include("Titular");
        query.whereEqualTo("objectId", folio);
        query.fromLocalDatastore();
        query.getFirstInBackground(new GetCallback<Documentos>() {
            public void done(Documentos doc, ParseException e) {
                if (e == null) {
                    // Results were successfully found from the local datastore.
                    txtFolio.setText(doc.getFolioProg().toString());
                    txtTitular.setText(doc.getTitular().getString("Nombre"));

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
        inspeccion.put("Reporte", "Error");
        inspeccion.put("Detalles", editReport.getText().toString());
        inspeccion.put("Inspector", ParseObject.createWithoutData("Inspectores", prefs.getInspector()));
        inspeccion.put("Documentos", ParseObject.createWithoutData("Documentos", folio));
        inspeccion.saveEventually();
        Toast.makeText(getActivity(),R.string.toast_sent,Toast.LENGTH_LONG).show();
        getActivity().onBackPressed();
    }

}
