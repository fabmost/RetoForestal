package com.clicky.semarnat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicky.semarnat.R;
import com.clicky.semarnat.data.Documentos;
import com.parse.ParseQueryAdapter;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 * Created by Clicky on 4/2/15.
 *
 */
public class DocumentosAdapter extends ParseQueryAdapter<Documentos> {

    private LayoutInflater inflater;
    private SimpleDateFormat format;

    public DocumentosAdapter(Context context, ParseQueryAdapter.QueryFactory<Documentos> queryFactory) {
        super(context, queryFactory);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        format = new SimpleDateFormat("dd-mm-yyyy", Locale.US);
    }

    @Override
    public View getItemView(Documentos documento, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_folio, parent, false);
            holder = new ViewHolder();
            holder.imgType = (ImageView) view.findViewById(R.id.img_folio);
            holder.txtFolio = (TextView) view.findViewById(R.id.txt_folio);
            holder.txtDate = (TextView) view.findViewById(R.id.txt_date);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if(documento.getTipo().equals("Remision")){
            holder.imgType.setImageResource(R.drawable.remision);
        }else if(documento.getTipo().equals("Reembarque")){
            holder.imgType.setImageResource(R.drawable.reembarques);
        }
        holder.txtFolio.setText(documento.getFolioProg().toString());
        holder.txtDate.setText(format.format(documento.getFechaExp()));

        return view;
    }

    private static class ViewHolder {
        ImageView imgType;
        TextView txtFolio;
        TextView txtDate;
    }

}
