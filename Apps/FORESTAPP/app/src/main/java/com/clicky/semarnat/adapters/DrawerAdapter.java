package com.clicky.semarnat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicky.semarnat.R;


/**
 *
 * Created by Clicky on 4/4/15.
 *
 */
public class DrawerAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater mInflater;

    private int reportes;
    private String[]items;
    private int[]images, selected;
    private int selectedIndex;

    public DrawerAdapter(Context context, String[] items, int[] images, int[] selected, int reportes) {
        this.context = context;
        this.items = items;
        this.images = images;
        this.selected = selected;
        this.reportes = reportes;
        selectedIndex = 1;

        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolderHeader{
        TextView txtNombre;
    }

    static class ViewHolder {
        ImageView imgMenu;
        TextView txtMenu;
        TextView txtReporte;
    }

    @Override
    public String getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return 0;
        else
            return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return items.length+1;
    }

    public void setSelectedIndex(int index) {
        selectedIndex = index;
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        View view = convertView;
        ViewHolder holder = null;
        ViewHolderHeader holderHeader = null;

        if (view == null) {
            switch (type) {
                case 0:
                    holderHeader = new ViewHolderHeader();
                    view = mInflater.inflate(R.layout.item_drawer_header, null);
                    holderHeader.txtNombre = (TextView)view.findViewById(R.id.txt_nombre);
                    view.setTag(holderHeader);
                    break;
                case 1:
                    holder = new ViewHolder();
                    view = mInflater.inflate(R.layout.item_drawer_icon, null);
                    holder.imgMenu = (ImageView)view.findViewById(R.id.img_menu);
                    holder.txtMenu = (TextView)view.findViewById(R.id.txt_menu);
                    holder.txtReporte = (TextView)view.findViewById(R.id.txt_report);
                    view.setTag(holder);
                    break;
            }
        } else {
            switch (type) {
                case 0:
                    holderHeader = (ViewHolderHeader)view.getTag();
                    break;
                case 1:
                    holder = (ViewHolder)view.getTag();
                    break;
            }
        }
        switch (type) {
            case 0:
                //holderHeader.txtNombre.setText("");
                break;
            case 1:
                if(selectedIndex == position){
                    holder.txtMenu.setTextColor(context.getResources().getColor(R.color.primary_dark));
                    holder.imgMenu.setImageResource(selected[position - 1]);
                }else {
                    holder.txtMenu.setTextColor(context.getResources().getColor(R.color.black));
                    holder.imgMenu.setImageResource(images[position - 1]);
                }
                holder.txtMenu.setText(items[position - 1]);
                if (position == items.length) {
                    holder.txtReporte.setVisibility(View.VISIBLE);
                    holder.txtReporte.setText("" + reportes);
                } else
                    holder.txtReporte.setVisibility(View.GONE);
                break;
        }

        return view;
    }
}
