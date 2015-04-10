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
public class EmpresasAdapter extends ArrayAdapter<ParseObject> {

    private LayoutInflater inflater;

    public EmpresasAdapter(Context context, int resource, List<ParseObject> items) {
        super(context, resource, items);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_empresa, parent, false);
            holder = new ViewHolder();
            holder.txtEmpresa = (TextView) view.findViewById(R.id.txt_empresa);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ParseObject empresa = getItem(position);

        holder.txtEmpresa.setText(empresa.getString("Nombre"));

        return view;
    }

    private static class ViewHolder {
        TextView txtEmpresa;
    }

}
