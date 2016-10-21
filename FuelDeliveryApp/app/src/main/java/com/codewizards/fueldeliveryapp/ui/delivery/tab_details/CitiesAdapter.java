package com.codewizards.fueldeliveryapp.ui.delivery.tab_details;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.codewizards.fueldeliveryapp.entities.City;

import java.util.List;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.Holder> {
    private List<City> cities;

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return cities != null ? cities.size() : 0;
    }

    class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }
}
