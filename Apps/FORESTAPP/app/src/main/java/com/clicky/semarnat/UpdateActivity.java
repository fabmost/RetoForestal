package com.clicky.semarnat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.clicky.semarnat.data.Documentos;
import com.clicky.semarnat.data.MateriaPrima;
import com.clicky.semarnat.data.SEMARNATPreferences;
import com.clicky.semarnat.utils.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

/**
 *
 * Created by Clicky on 4/3/15.
 *
 */
public class UpdateActivity extends ActionBarActivity {

    private SEMARNATPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        prefs = new SEMARNATPreferences(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(Utils.isConnected(this)){
            loadDocumentos();
        }
    }

    private void toMain(){
        Intent i = null;
        String rol = prefs.getRol();
        prefs.setIsFirst(false);
        switch (rol) {
            case "Titular":
                i = new Intent(UpdateActivity.this, TitularMainActivity.class);
                break;
            case "Transportista":
                i = new Intent(UpdateActivity.this, TransportistaMainActivity.class);
                break;
            case "CAT":
                i = new Intent(UpdateActivity.this, CATMainActivity.class);
                break;
        }
        startActivity(i);
        finish();
    }

    private void loadDocumentos() {
        ParseQuery<Documentos> query = Documentos.getQuery();
        query.include("Titular");
        query.include("Transportista");
        query.include("CAT");
        switch (prefs.getRol()){
            case "Titular":
                query.whereEqualTo("Titular", ParseObject.createWithoutData("Titular", prefs.getUser()));
                break;
            case "Transportista":
                query.whereEqualTo("Transportista", ParseObject.createWithoutData("Transportistas", prefs.getUser()));
                break;
            case "CAT":
                query.whereEqualTo("CAT", ParseObject.createWithoutData("CAT", prefs.getUser()));
                break;
        }
        query.findInBackground(new FindCallback<Documentos>() {
            public void done(final List<Documentos> docs, ParseException e) {
                if (e == null) {
                    if(!docs.isEmpty()){
                        prefs.setLastUpdate(docs.get(0).getUpdatedAt().toString());
                    }
                    ParseObject.pinAllInBackground(docs, new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                if (!isFinishing()) {
                                    loadMateriaPrima(docs);
                                }
                            } else {
                                Log.i("TodoListActivity", "Error pinning todos: " + e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.i("TodoListActivity","loadDocumentos: Error finding pinned todos: "+ e.getMessage());
                }
            }
        });
    }

    private void loadMateriaPrima(final List<Documentos> docList) {
        ParseQuery<MateriaPrima> query = MateriaPrima.getQuery();
        query.whereContainedIn("Documentos",docList);
        query.findInBackground(new FindCallback<MateriaPrima>() {
            public void done(List<MateriaPrima> materia, ParseException e) {
                if (e == null) {
                    ParseObject.pinAllInBackground(materia, new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                if (!isFinishing()) {
                                    if(prefs.getRol().equals("Titular"))
                                        loadReportes(docList);
                                    else if(prefs.getRol().equals("CAT"))
                                        loadInventario();
                                    else
                                        toMain();
                                }
                            } else {
                                Log.i("TodoListActivity", "Error pinning todos: " + e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.i("TodoListActivity","loadMateriaPrima: Error finding pinned todos: "+ e.getMessage());
                }
            }
        });
    }

    private void loadReportes(List<Documentos> docList){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Inspecciones");
        query.include("Inspector");
        query.whereNotEqualTo("Detalles","");
        query.whereContainedIn("Documentos",docList);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> materia, ParseException e) {
                if (e == null) {
                    ParseObject.pinAllInBackground(materia, new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                if (!isFinishing()) {
                                    loadTransportistas();
                                }
                            } else {
                                Log.i("TodoListActivity", "Error pinning todos: " + e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.i("TodoListActivity","loadMateriaPrima: Error finding pinned todos: "+ e.getMessage());
                }
            }
        });
    }

    private void loadTransportistas(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Transportistas");
        query.whereEqualTo("Titular",ParseObject.createWithoutData("Titular",prefs.getUser()));
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> transportistas, ParseException e) {
                if(e == null) {
                    ParseObject.pinAllInBackground(transportistas, new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                if (!isFinishing()) {
                                    loadCATs();
                                }
                            } else {
                                Log.i("TodoListActivity", "Error pinning todos: " + e.getMessage());
                            }
                        }
                    });
                }else{
                    Log.i("TodoListActivity","loadTransportistas: Error finding pinned todos: "+ e.getMessage());
                }
            }
        });
    }

    private void loadCATs(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CAT");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> transportistas, ParseException e) {
                if(e == null) {
                    ParseObject.pinAllInBackground(transportistas, new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                if (!isFinishing()) {
                                    toMain();
                                }
                            } else {
                                Log.i("TodoListActivity", "Error pinning todos: " + e.getMessage());
                            }
                        }
                    });
                }else{
                    Log.i("TodoListActivity","loadCATs: Error finding pinned todos: "+ e.getMessage());
                }
            }
        });
    }

    private void loadInventario(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Inventario");
        query.whereEqualTo("CAT",ParseObject.createWithoutData("CAT",prefs.getUser()));
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> inventario, ParseException e) {
                if(e == null) {
                    ParseObject.pinAllInBackground(inventario, new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                if (!isFinishing()) {
                                    toMain();
                                }
                            } else {
                                Log.i("TodoListActivity", "Error pinning todos: " + e.getMessage());
                            }
                        }
                    });
                }else{
                    Log.i("TodoListActivity","loadTransportistas: Error finding pinned todos: "+ e.getMessage());
                }
            }
        });
    }

}
