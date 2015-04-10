package com.clicky.semarnat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.clicky.semarnat.adapters.DocumentosAdapter;
import com.clicky.semarnat.data.Documentos;
import com.clicky.semarnat.utils.Utils;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 *
 * Created by Clicky on 3/24/15.
 *
 */
public class TransportistaMainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    private static String EXTRA_FOLIO = "folio";

    private DocumentosAdapter adapter;
    private LinearLayout emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportista_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView list = (ListView)findViewById(R.id.listView);
        emptyView = (LinearLayout)findViewById(R.id.layout_empty);

        ParseQueryAdapter.QueryFactory<Documentos> factory = new ParseQueryAdapter.QueryFactory<Documentos>() {
            public ParseQuery<Documentos> create() {
                ParseQuery<Documentos> query = Documentos.getQuery();
                query.fromLocalDatastore();
                return query;
            }
        };
        adapter = new DocumentosAdapter(this, factory);

        list.setEmptyView(emptyView);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
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
        Intent i = new Intent(TransportistaMainActivity.this, TransportistaDetallesActivity.class);
        i.putExtra(EXTRA_FOLIO, adapter.getItem(position).getObjectId());
        startActivity(i);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    private void update(){
        Intent i = new Intent(TransportistaMainActivity.this, UpdateActivity.class);
        startActivity(i);
        finish();
    }

    private void logOut(){
        Utils.logOut(this);
    }

}
