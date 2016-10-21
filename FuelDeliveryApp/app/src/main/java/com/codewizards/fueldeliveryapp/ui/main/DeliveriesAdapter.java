package com.codewizards.fueldeliveryapp.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codewizards.fueldeliveryapp.R;
import com.codewizards.fueldeliveryapp.entities.Delivery;

import java.util.List;

import butterknife.Bind;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class DeliveriesAdapter extends RecyclerView.Adapter<DeliveriesAdapter.Holder> {
    private List<Delivery> deliveries;

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return deliveries != null ? deliveries.size() : 0;
    }

    class Holder extends RecyclerView.ViewHolder {
        @Bind(R.id.tvDeliveryName) TextView tvDeliveryName;
        @Bind(R.id.tvOrdersCount) TextView tvOrderCount;
        public Holder(View itemView) {
            super(itemView);
        }
    }
}
