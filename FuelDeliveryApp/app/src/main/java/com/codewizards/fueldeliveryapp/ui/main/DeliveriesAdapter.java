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
import butterknife.ButterKnife;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class DeliveriesAdapter extends RecyclerView.Adapter<DeliveriesAdapter.Holder> {
    private List<Delivery> deliveries;
    private OnDeliveryClickListener onDeliveryClickListener;

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if(deliveries != null && deliveries.size() > position && deliveries.get(position) != null) {
            Delivery delivery = deliveries.get(position);
            int ordersCount = delivery.getOrders() != null ? delivery.getOrders().size() : 0;
            holder.tvDeliveryName.setText(delivery.getName());
            holder.tvOrderCount.setText(String.valueOf(ordersCount));
            holder.root.setTag(delivery);
            holder.root.setOnClickListener(view -> {
                Delivery d = (Delivery) view.getTag();
                onDeliveryClickListener.onDeliveryClick(d);
            });
        }
    }

    @Override
    public int getItemCount() {
        return deliveries != null ? deliveries.size() : 0;
    }

    public void setData(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public void clearData() {
        if(deliveries != null) {
            deliveries.clear();
        }
    }

    public void setOnDeliveryClickListener(OnDeliveryClickListener onDeliveryClickListener) {
        this.onDeliveryClickListener = onDeliveryClickListener;
    }

    class Holder extends RecyclerView.ViewHolder {
        @Bind(R.id.root) View root;
        @Bind(R.id.tvDeliveryName) TextView tvDeliveryName;
        @Bind(R.id.tvOrdersCount) TextView tvOrderCount;
        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
