package com.clicky.semarnat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.clicky.semarnat.adapters.EmpresasAdapter;
import com.clicky.semarnat.adapters.TransportistasAdapter;
import com.clicky.semarnat.data.Documentos;
import com.clicky.semarnat.data.SEMARNATPreferences;
import com.clicky.semarnat.utils.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Clicky on 3/24/15.
 *
 */
public class CATMainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener{

    private static String EXTRA_EMPRESAID = "empresaId";
    private static String EXTRA_EMPRESA = "empresa";

    private SEMARNATPreferences prefs;
    private EmpresasAdapter adapter;

    private ListView list;
    private LinearLayout emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListView)findViewById(R.id.listView);
        emptyView = (LinearLayout)findViewById(R.id.layout_empty);
        ImageButton fab = (ImageButton)findViewById(R.id.fab);

        prefs = new SEMARNATPreferences(this);

        ParseQuery<Documentos> query = Documentos.getQuery();
        query.include("Titular");
        query.include("Transportista");
        query.include("CAT");
        query.whereEqualTo("CAT",ParseObject.createWithoutData("CAT",prefs.getUser()));
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<Documentos>() {
            public void done(final List<Documentos> docs, ParseException e) {
                if(e == null) {
                    List<ParseObject> listTitulares = new ArrayList<>();
                    for(Documentos doc:docs) {
                        ParseObject titular = doc.getTitular();
                        if(!listTitulares.contains(titular))
                            listTitulares.add(titular);
                    }
                    adapter = new EmpresasAdapter(CATMainActivity.this, R.layout.item_empresa, listTitulares);
                    list.setEmptyView(emptyView);
                    list.setAdapter(adapter);
                }else{
                    Log.i("TodoListActivity", "loadTransportistas: Error finding pinned todos: " + e.getMessage());
                }
            }
        });

        //list.setEmptyView(emptyView);
        //list.setAdapter(adapter);
        list.setOnItemClickListener(this);

        fab.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_transportista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                update();
                return true;
            case R.id.action_logout:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(CATMainActivity.this, CATDetallesActivity.class);
        i.putExtra(EXTRA_EMPRESAID, adapter.getItem(position).getObjectId());
        i.putExtra(EXTRA_EMPRESA, adapter.getItem(position).getString("Nombre"));
        startActivity(i);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    @Override
    public void onClick(View v){
        Intent i = new Intent(CATMainActivity.this, CATRegistroActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    private void update(){
        Intent i = new Intent(CATMainActivity.this, UpdateActivity.class);
        startActivity(i);
        finish();
    }

    private void logOut(){
        Utils.logOut(this);
    }

}
