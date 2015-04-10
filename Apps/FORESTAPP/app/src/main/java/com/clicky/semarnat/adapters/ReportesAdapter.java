package com.clicky.semarnat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.clicky.semarnat.R;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 *
 * Created by Clicky on 4/4/15.
 *
 */
public class ReportesAdapter extends ArrayAdapter<ParseObject> {

    private Context context;
    private int resource;
    private List<ParseObject> items, orig;
    private SimpleDateFormat format;

    public ReportesAdapter(Context context, int resource, List<ParseObject> items) {
        super(context, resource, items);

        this.context = context;
        this.resource = resource;
        this.items = items;

        format = new SimpleDateFormat("dd-mm-yyyy", Locale.US);
    }

    static class ViewHolder {
        TextView txtReporte;
        TextView txtFecha;
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

            holder.txtReporte = (TextView) view.findViewById(R.id.txt_folio);
            holder.txtFecha = (TextView) view.findViewById(R.id.txt_date);
            view.setTag(holder);


        } else
            holder = (ViewHolder) view.getTag();

        ParseObject reporte = items.get(position);

        holder.txtReporte.setText(reporte.getString("Reporte"));
        holder.txtFecha.setText(format.format(reporte.getCreatedAt()));

        return view;
    }
}