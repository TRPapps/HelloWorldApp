package com.trpapps.helloworldapp;

import java.util.ArrayList;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.Filter;
import android.widget.TextView;

/**
 * Created by rafal on 2015-07-06. :D
 */
public class BeersAdapter extends BaseAdapter implements Filterable {

    public Context context;
    public ArrayList<Beer> beerArrayList;
    public ArrayList<Beer> orig;

    public BeersAdapter(Context context, ArrayList<Beer> beerArrayList) {
        super();
        this.context = context;
        this.beerArrayList = beerArrayList;
    }

    public class BeerHolder
    {
        TextView name;
        TextView alc;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Beer> results = new ArrayList<>();

                if (orig == null)
                    orig = beerArrayList;

                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Beer g : orig) {
                            if (g.getName().toLowerCase().contains(constraint.toString()))
                                results.add(g);
                        }
                    }

                    oReturn.values = results;
                }

                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                beerArrayList = (ArrayList<Beer>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return beerArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return beerArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BeerHolder holder;

        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
            holder = new BeerHolder();
            holder.name = (TextView) convertView.findViewById(R.id.beerName);
            holder.alc = (TextView) convertView.findViewById(R.id.beerAlc);
            convertView.setTag(holder);
        }
        else
        {
            holder = (BeerHolder) convertView.getTag();
        }

        holder.name.setText(beerArrayList.get(position).getName());
        holder.alc.setText(String.valueOf(beerArrayList.get(position).getAlc()));

        return convertView;
    }
}
