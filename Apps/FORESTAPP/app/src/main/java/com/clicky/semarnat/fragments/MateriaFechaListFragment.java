package com.clicky.semarnat.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.clicky.semarnat.R;
import com.clicky.semarnat.adapters.MateriaFechaAdapter;
import com.clicky.semarnat.data.Documentos;
import com.clicky.semarnat.data.MateriaPrima;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 *
 * Created by Clicky on 4/4/15.
 *
 */
public class MateriaFechaListFragment extends Fragment {

    private static String EXTRA_EMPRESA = "empresa";
    private static String EXTRA_TIPO = "tipo";

    private OnMateriaSelectedListener mCallBack;

    private ListView list;
    private LinearLayout emptyView;
    private MateriaFechaAdapter adapter;

    private String empresa;
    private int tipo;

    public interface OnMateriaSelectedListener{
        public void onMateriaSelected(String materia, String doc);
    }

    public static MateriaFechaListFragment newInstance(String empresa, int tipo){
        MateriaFechaListFragment frag = new MateriaFechaListFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_EMPRESA, empresa);
        args.putInt(EXTRA_TIPO, tipo);
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

        //adapter = new ListAdapter(getActivity(), R.layout.item_list, folioList);
        // Set up the Parse query to use in the adapter
        ParseQueryAdapter.QueryFactory<MateriaPrima> factory = new ParseQueryAdapter.QueryFactory<MateriaPrima>() {
            public ParseQuery<MateriaPrima> create() {
                ParseQuery<Documentos> innerQuery = Documentos.getQuery();
                innerQuery.whereEqualTo("Titular", ParseObject.createWithoutData("Titular", empresa));
                switch (tipo){
                    case 0:
                        innerQuery.whereEqualTo("Tipo","Reembarque");
                        break;
                    case 1:
                        innerQuery.whereEqualTo("Tipo","Remision");
                        break;
                }
                innerQuery.fromLocalDatastore();
                ParseQuery<MateriaPrima> query = MateriaPrima.getQuery();
                query.whereMatchesQuery("Documentos", innerQuery);
                query.fromLocalDatastore();
                return query;
            }
        };
        adapter = new MateriaFechaAdapter(getActivity(), factory);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        list.setEmptyView(emptyView);
        list.setAdapter(adapter);
        if(tipo != 2) {
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mCallBack.onMateriaSelected(adapter.getItem(position).getObjectId(),
                            adapter.getItem(position).getDocumento().getObjectId());
                }
            });
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallBack = (OnMateriaSelectedListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()+ "Exception");
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mCallBack = null;
    }

    private void decodeArguments(){
        Bundle args = getArguments();
        if(args != null) {
            empresa = args.getString(EXTRA_EMPRESA);
            tipo = args.getInt(EXTRA_TIPO);
        }
    }

}