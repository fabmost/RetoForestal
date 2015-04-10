package com.clicky.semarnat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicky.semarnat.R;
import com.clicky.semarnat.data.Documentos;
import com.clicky.semarnat.data.MateriaPrima;
import com.parse.ParseQueryAdapter;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 * Created by Clicky on 4/3/15.
 *
 */
public class MateriaImgAdapter extends ParseQueryAdapter<MateriaPrima> {

    private LayoutInflater inflater;
    private SimpleDateFormat format;

    public MateriaImgAdapter(Context context, ParseQueryAdapter.QueryFactory<MateriaPrima> queryFactory) {
        super(context, queryFactory);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        format = new SimpleDateFormat("dd-mm-yyyy", Locale.US);
    }

    @Override
    public View getItemView(MateriaPrima materia, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_materia_img, parent, false);
            holder = new ViewHolder();
            holder.imgMateria = (ImageView) view.findViewById(R.id.img_materia);
            holder.txtMateria = (TextView) view.findViewById(R.id.txt_materia);
            holder.txtDate = (TextView) view.findViewById(R.id.txt_date);
            holder.txtCant = (TextView) view.findViewById(R.id.txt_cant);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Documentos documento = materia.getDocumento();

        if(documento.getTipo().equals("Remision")){
            holder.imgMateria.setImageResource(R.drawable.remision);
        }else if(documento.getTipo().equals("Reembarque")){
            holder.imgMateria.setImageResource(R.drawable.reembarques);
        }
        holder.txtMateria.setText(materia.getTipo());
        holder.txtDate.setText(format.format(materia.getCreatedAt()));
        holder.txtCant.setText(materia.getVolumen() + " " + materia.getUnidad());

        return view;
    }

    private static class ViewHolder {
        ImageView imgMateria;
        TextView txtMateria;
        TextView txtDate;
        TextView txtCant;
    }

}
