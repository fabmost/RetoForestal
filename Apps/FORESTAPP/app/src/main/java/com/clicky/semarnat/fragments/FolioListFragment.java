package com.clicky.semarnat.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.clicky.semarnat.R;
import com.clicky.semarnat.adapters.DocumentosAdapter;
import com.clicky.semarnat.data.Documentos;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 *
 * Created by Clicky on 3/22/15.
 *
 */
public class FolioListFragment extends Fragment {

    private OnFolioSelectedListener mCallBack;

    private ListView list;
    private LinearLayout emptyView;
    private DocumentosAdapter adapter;

    public interface OnFolioSelectedListener{
        public void onAltaFolio();
        public void onFolioSelected(String folio);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_fab, container, false);

        list = (ListView)v.findViewById(R.id.listView);
        emptyView = (LinearLayout)v.findViewById(R.id.layout_empty);

        ImageButton fab = (ImageButton)v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onAltaFolio();
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //adapter = new ListAdapter(getActivity(), R.layout.item_list, folioList);
        // Set up the Parse query to use in the adapter
        ParseQueryAdapter.QueryFactory<Documentos> factory = new ParseQueryAdapter.QueryFactory<Documentos>() {
            public ParseQuery<Documentos> create() {
                ParseQuery<Documentos> query = Documentos.getQuery();
                query.fromLocalDatastore();
                return query;
            }
        };
        adapter = new DocumentosAdapter(getActivity(), factory);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        list.setEmptyView(emptyView);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallBack.onFolioSelected(adapter.getItem(position).getObjectId());
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallBack = (OnFolioSelectedListener) activity;
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
