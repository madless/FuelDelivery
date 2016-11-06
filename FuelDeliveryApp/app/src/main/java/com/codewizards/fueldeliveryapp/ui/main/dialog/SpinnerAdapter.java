package com.codewizards.fueldeliveryapp.ui.main.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codewizards.fueldeliveryapp.entities.City;

import java.util.List;

/**
 * Created by Интернет on 05.11.2016.
 */

public class SpinnerAdapter extends ArrayAdapter<City> {
    private Context context;
    private int resource;
    private int textViewResourceId;
    private List<City> cities;
    public SpinnerAdapter(Context context, int resource, int textViewResourceId, List<City> cities) {
        super(context, resource, textViewResourceId, cities);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.cities = cities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(textViewResourceId);
        tv.setText(cities.get(position).getName());
        return convertView;
    }


}
