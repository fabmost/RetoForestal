package com.clicky.semarnat.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.clicky.semarnat.R;
import com.clicky.semarnat.adapters.ReportesAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 *
 * Created by Clicky on 4/4/15.
 *
 */
public class ReportesListFragment extends Fragment {

    private OnReporteSelectedListener mCallBack;

    private ListView list;
    private LinearLayout emptyView;
    private ReportesAdapter adapter;

    public interface OnReporteSelectedListener{
        public void onReporteSelected(String reporte, String documento);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        list = (ListView)v.findViewById(R.id.listView);
        emptyView = (LinearLayout)v.findViewById(R.id.layout_empty);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Set up the Parse query to use in the adapter
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Inspecciones");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> reportes, ParseException e) {
                if(e == null) {
                    adapter = new ReportesAdapter(getActivity(), R.layout.item_folio, reportes);
                    list.setEmptyView(emptyView);
                    list.setAdapter(adapter);
                }else{
                    Log.i("TodoListActivity", "loadTransportistas: Error finding pinned todos: " + e.getMessage());
                }
            }
        });


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallBack.onReporteSelected(adapter.getItem(position).getObjectId(),
                        adapter.getItem(position).getParseObject("Documentos").getObjectId());
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallBack = (OnReporteSelectedListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()+ "Exception");
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mCallBack = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        // Get the SearchView and set the searchable configuration
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // Assumes current activity is the searchable activity
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
