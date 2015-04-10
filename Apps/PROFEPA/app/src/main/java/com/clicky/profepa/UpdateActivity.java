package com.clicky.profepa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.clicky.profepa.data.Documentos;
import com.clicky.profepa.data.MateriaPrima;
import com.clicky.profepa.data.PROFEPAPreferences;
import com.clicky.profepa.utils.Utils;
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
public class UpdateActivity extends ActionBarActivity{

    private PROFEPAPreferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        pref = new PROFEPAPreferences(UpdateActivity.this);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(Utils.isConnected(this)){
            loadDocumentos();
        }
    }

    private void toMain(){
        pref.setIsFirst(false);
        Intent i = new Intent(UpdateActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void loadDocumentos() {
        ParseQuery<Documentos> query = Documentos.getQuery();
        query.include("Titular");
        query.include("Transportista");
        query.include("CAT");
        query.findInBackground(new FindCallback<Documentos>() {
            public void done(List<Documentos> docs, ParseException e) {
                if (e == null) {
                    if(!docs.isEmpty()){
                        pref.setLastUpdateDocs(docs.get(0).getUpdatedAt().toString());
                    }
                    ParseObject.pinAllInBackground(docs,new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                if (!isFinishing()) {
                                    loadMateriaPrima();
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

    private void loadMateriaPrima() {
        ParseQuery<MateriaPrima> query = MateriaPrima.getQuery();
        query.findInBackground(new FindCallback<MateriaPrima>() {
            public void done(List<MateriaPrima> materia, ParseException e) {
                if (e == null) {
                    if(!materia.isEmpty()){
                        pref.setLastUpdateMat(materia.get(0).getUpdatedAt().toString());
                    }
                    ParseObject.pinAllInBackground(materia,new SaveCallback() {
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
                } else {
                    Log.i("TodoListActivity","loadMateriaPrima: Error finding pinned todos: "+ e.getMessage());
                }
            }
        });
    }

}
