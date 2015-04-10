package com.clicky.semarnat.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clicky.semarnat.R;
import com.clicky.semarnat.data.Documentos;
import com.clicky.semarnat.data.MateriaPrima;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 *
 * Created by Clicky on 4/4/15.
 *
 */
public class TransportistaInfoFragment extends Fragment{

    private static String EXTRA_FOLIO = "folio";

    private MapView mMapView;
    private GoogleMap mMap;

    private String folio;
    private TextView txtDireccion, txtNombre, txtFecha, txtTipo, txtCantidad, txtDesc, txtVol, txtUnidad;

    public static TransportistaInfoFragment newInstance(String folio){
        TransportistaInfoFragment frag = new TransportistaInfoFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_FOLIO, folio);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_transportista_info, container, false);

        mMapView = (MapView) v.findViewById(R.id.map);

        txtDireccion = (TextView)v.findViewById(R.id.txt_direccion);
        txtNombre = (TextView)v.findViewById(R.id.txt_nombre);
        txtFecha = (TextView)v.findViewById(R.id.txt_fecha);
        txtTipo = (TextView)v.findViewById(R.id.txt_tipo);
        txtCantidad = (TextView)v.findViewById(R.id.txt_cant);
        txtDesc = (TextView)v.findViewById(R.id.txt_desc);
        txtVol = (TextView)v.findViewById(R.id.txt_vol);
        txtUnidad = (TextView)v.findViewById(R.id.txt_unidad);

        mMapView.onCreate(savedInstanceState);

        setUpMapIfNeeded();

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decodeArguments();
        findFolio();
    }

    private void decodeArguments(){
        Bundle args = getArguments();
        if(args != null) {
            folio = args.getString(EXTRA_FOLIO);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.

            mMapView.onResume();
            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            mMap = mMapView.getMap();
            mMap.getUiSettings().setScrollGesturesEnabled(false);
        }
    }

    private void findFolio(){
        ParseQuery<Documentos> query = Documentos.getQuery();
        query.whereEqualTo("objectId", folio);
        query.include("CAT");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<Documentos>() {
            public void done(final List<Documentos> docList, ParseException e) {
                if (e == null) {
                    SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy", Locale.US);
                    // Results were successfully found from the local datastore.
                    ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(docList.get(0).getFolioProg().toString());

                    ParseObject cat = docList.get(0).getCAT();
                    txtDireccion.setText(cat.getString("Domicilio"));
                    txtNombre.setText(cat.getString("Nombre"));
                    txtFecha.setText(format.format(docList.get(0).getFechaVen()));

                    getAddress(cat.getString("Domicilio"));
                } else {
                    // There was an error.
                    Log.i("ResultFragment", "findFolio: Error finding document: " + e.getMessage());
                }
            }
        });

        ParseQuery<MateriaPrima> query2 = MateriaPrima.getQuery();
        query2.whereEqualTo("Documentos", ParseObject.createWithoutData("Documentos", folio));
        query2.fromLocalDatastore();
        query2.findInBackground(new FindCallback<MateriaPrima>() {
            public void done(final List<MateriaPrima> materiaList, ParseException e) {
                if (e == null) {
                    // Results were successfully found from the local datastore.
                    MateriaPrima materia = materiaList.get(0);
                    txtTipo.setText(materia.getTipo());
                    txtCantidad.setText(materia.getCantidad().toString());
                    txtDesc.setText(materia.getDescripcion());
                    txtVol.setText(materia.getVolumen());
                    txtUnidad.setText(materia.getUnidad());
                } else {
                    // There was an error.
                    Log.i("ResultFragment", "findFolio: Error finding document: " + e.getMessage());
                }
            }
        });
    }

    private void getAddress(String address){
        if (mMap != null) {
            //setUpMap();
            Geocoder geocoder = new Geocoder(getActivity());
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocationName(address, 1);
                if (addresses.size() > 0) {
                    double latitude = addresses.get(0).getLatitude();
                    double longitude = addresses.get(0).getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                }
            }catch(IOException e){Log.e("INFO",e.toString());}
        }
    }

}
