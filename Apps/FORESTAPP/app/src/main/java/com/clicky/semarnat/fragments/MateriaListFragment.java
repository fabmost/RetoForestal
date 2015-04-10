package com.clicky.semarnat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.clicky.semarnat.R;
import com.clicky.semarnat.adapters.MateriaAdapter;
import com.clicky.semarnat.adapters.TransportistasAdapter;
import com.clicky.semarnat.data.Documentos;
import com.clicky.semarnat.data.MateriaPrima;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.List;

/**
 *
 * Created by Clicky on 4/4/15.
 *
 */
public class MateriaListFragment extends Fragment{

    private static String EXTRA_EMPRESA = "empresa";

    private ListView list;
    private LinearLayout emptyView;
    private MateriaAdapter adapter;

    private String empresa;

    public static MateriaListFragment newInstance(String empresa){
        MateriaListFragment frag = new MateriaListFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_EMPRESA, empresa);
        frag.setArguments(args);
        return frag;
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

        decodeArguments();
        //findFolio();

        // Set up the Parse query to use in the adapter
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Inventario");
        query.whereEqualTo("Titular",ParseObject.createWithoutData("Titular",empresa));
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> inventario, ParseException e) {
                if(e == null) {
                    adapter = new MateriaAdapter(getActivity(), R.layout.item_materia, inventario);
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

        list.setEmptyView(emptyView);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //mCallBack.onFolioSelected(adapter.getItem(position).getObjectId());
            }
        });
    }

    private void decodeArguments(){
        Bundle args = getArguments();
        if(args != null) {
            empresa = args.getString(EXTRA_EMPRESA);
        }
    }

}
