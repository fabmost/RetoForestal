package com.clicky.semarnat;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clicky.semarnat.data.Documentos;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

/**
 *
 * Created by Clicky on 4/5/15.
 *
 */
public class CATValidarActivity extends ActionBarActivity{

    private static String EXTRA_DOCUMENTO = "docId";

    private TextView txtFolio, txtTitular;
    private EditText editReport;

    private Documentos documento;
    private String docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_validar);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        docId = getIntent().getExtras().getString(EXTRA_DOCUMENTO);

        txtFolio = (TextView)findViewById(R.id.txt_folio);
        txtTitular = (TextView)findViewById(R.id.txt_titular);
        editReport = (EditText)findViewById(R.id.edit_reporte);

        getFolio();
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
                send();
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

    private void getFolio(){
        ParseQuery<Documentos> query = Documentos.getQuery();
        query.include("Titular");
        query.whereEqualTo("objectId", docId);
        query.fromLocalDatastore();
        query.getFirstInBackground(new GetCallback<Documentos>() {
            public void done(Documentos doc, ParseException e) {
                if (e == null) {
                    // Results were successfully found from the local datastore.
                    documento = doc;
                    txtFolio.setText(doc.getFolioProg().toString());
                    txtTitular.setText(doc.getTitular().getString("Nombre"));

                } else {
                    // There was an error.
                    Log.i("ResultFragment", "findFolio: Error finding document: " + e.getMessage());
                }
            }
        });
    }

    private void send(){
        documento.put("Observaciones",editReport.getText().toString());
        documento.saveEventually();
        documento.pinInBackground();

        Toast.makeText(this, R.string.toast_sent, Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

}
