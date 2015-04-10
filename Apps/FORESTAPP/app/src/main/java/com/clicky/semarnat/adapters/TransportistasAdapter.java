package com.clicky.semarnat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.clicky.semarnat.R;
import com.parse.ParseObject;

import java.util.List;

/**
 *
 * Created by Clicky on 4/4/15.
 *
 */
public class TransportistasAdapter extends ArrayAdapter<ParseObject> {

    private Context context;
    private int resource;
    private List<ParseObject> items, orig;

    public TransportistasAdapter(Context context, int resource, List<ParseObject> items) {
        super(context, resource, items);

        this.context = context;
        this.resource = resource;
        this.items = items;
    }

    static class ViewHolder {
        TextView txtTransportista;
    }

    @Override
    public ParseObject getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);

            holder.txtTransportista = (TextView) view.findViewById(R.id.txt_empresa);
            view.setTag(holder);


        } else
            holder = (ViewHolder) view.getTag();

        ParseObject transportista = items.get(position);

        holder.txtTransportista.setText(transportista.getString("Chofer"));

        return view;
    }
}
