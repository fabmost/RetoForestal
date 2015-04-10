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
import com.clicky.semarnat.adapters.MateriaImgAdapter;
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
public class MateriaImgListFragment extends Fragment {

    private static String EXTRA_EMPRESA = "empresa";

    private OnMateriaSelectedListener mCallBack;

    private ListView list;
    private LinearLayout emptyView;
    private MateriaImgAdapter adapter;

    private String empresa;

    public interface OnMateriaSelectedListener{
        public void onMateriaSelected(String materia, String doc);
    }

    public static MateriaImgListFragment newInstance(String empresa){
        MateriaImgListFragment frag = new MateriaImgListFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_EMPRESA, empresa);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_image, container, false);

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
                innerQuery.fromLocalDatastore();
                ParseQuery<MateriaPrima> query = MateriaPrima.getQuery();
                query.include("Documentos");
                query.whereMatchesQuery("Documentos", innerQuery);
                query.fromLocalDatastore();
                return query;
            }
        };
        adapter = new MateriaImgAdapter(getActivity(), factory);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        list.setEmptyView(emptyView);
        list.setAdapter(adapter);
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
        }
    }

}