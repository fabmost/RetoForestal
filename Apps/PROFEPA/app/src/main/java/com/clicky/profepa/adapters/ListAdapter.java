package com.clicky.profepa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicky.profepa.R;
import com.clicky.profepa.data.Folio;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Clicky on 3/22/15.
 *
 */
public class ListAdapter extends ArrayAdapter<Folio> {

    private Context context;
    private int resource;
    private List<Folio> items, orig;

    public ListAdapter(Context context, int resource, List<Folio> items) {
        super(context, resource, items);

        this.context = context;
        this.resource = resource;
        this.items = items;
    }

    static class ViewHolder {
        ImageView imgFolio;
        TextView txtFolio;
        TextView txtFecha;
    }

    @Override
    public Folio getItem(int position) {
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

            holder.imgFolio = (ImageView) view.findViewById(R.id.img_folio);
            holder.txtFolio = (TextView) view.findViewById(R.id.txt_folio);
            holder.txtFecha = (TextView) view.findViewById(R.id.txt_date);
            view.setTag(holder);


        } else
            holder = (ViewHolder) view.getTag();

        Folio folio = items.get(position);

        holder.imgFolio.setImageResource(R.mipmap.ic_launcher);
        holder.txtFolio.setText(folio.getFolio());
        holder.txtFecha.setText(folio.getFecha());
        //holder.txtTel.setText(site.getTel());

        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<Folio> results = new ArrayList<>();
                if (orig == null)
                    orig = items;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Folio g : orig) {
                            if (g.getFolio().toLowerCase().replace(" ", "").contains(constraint.toString().toLowerCase()) )
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                    oReturn.count = results.size();
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.count > 0) {
                    items = (ArrayList<Folio>) results.values;
                    notifyDataSetChanged();
                } else {
                    items.clear();
                    notifyDataSetChanged();
                    notifyDataSetInvalidated();
                }

            }

        };
    }
}