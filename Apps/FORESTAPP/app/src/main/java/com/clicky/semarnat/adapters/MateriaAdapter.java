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
 * Created by Clicky on 4/3/15.
 *
 */
public class MateriaAdapter extends ArrayAdapter<ParseObject> {

    private Context context;
    private int resource;
    private List<ParseObject> items;

    public MateriaAdapter(Context context, int resource, List<ParseObject> items) {
        super(context, resource, items);

        this.context = context;
        this.resource = resource;
        this.items = items;
    }

    static class ViewHolder {
        TextView txtMateria;
        TextView txtCant;
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

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);

            holder.txtMateria = (TextView) view.findViewById(R.id.txt_materia);
            holder.txtCant = (TextView) view.findViewById(R.id.txt_cant);
            view.setTag(holder);


        } else
            holder = (ViewHolder) view.getTag();

        ParseObject inventario = items.get(position);

        holder.txtMateria.setText(inventario.getString("Tipo_mat"));
        holder.txtCant.setText(inventario.getNumber("cantidad").toString());

        return view;
    }
}
