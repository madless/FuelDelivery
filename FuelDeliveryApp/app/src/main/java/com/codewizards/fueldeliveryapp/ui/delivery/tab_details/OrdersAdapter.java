package com.codewizards.fueldeliveryapp.ui.delivery.tab_details;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codewizards.fueldeliveryapp.R;
import com.codewizards.fueldeliveryapp.entities.City;
import com.codewizards.fueldeliveryapp.entities.FuzzyNumber;
import com.codewizards.fueldeliveryapp.entities.Order;
import com.codewizards.fueldeliveryapp.utils.calculator.FuzzyNumberHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.Holder> {
    private static final String FUZZY_NUMBER_FORMAT = "(%d, %d, %d)";
    private List<Order> orders;

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Context context = holder.tvCityName.getContext();
        if(orders != null && orders.size() > position && orders.get(position) != null) {
            Order order = orders.get(position);
            FuzzyNumber fuel = order.getAmountOfFuel();
            City city = order.getCity();
            if(fuel != null) {
                holder.tvFuzzyNumber.setText(String.format(FUZZY_NUMBER_FORMAT, fuel.getX1(), fuel.getX0(), fuel.getX2()));
            }
            if(city != null) {
                holder.tvCityName.setText(order.getCity().getName());
            }
            if(order.getAmountOfFuelBeforeOrder() != null) {
                holder.tvFuelBeforeOrder.setText(order.getAmountOfFuelBeforeOrder().toString());
            } else {
                holder.tvFuelBeforeOrder.setText("No data!");
            }
            if(!FuzzyNumberHelper.isFuzzyValid(order.getAmountOfFuelAfterOrder())) {
                holder.tvFuelBeforeOrder.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                holder.tvCityName.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                holder.tvFuzzyNumber.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
            }
        }
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    public void setData(List<Order> orders) {
        this.orders = orders;
    }

    public void clearData() {
        if(orders != null) {
            orders.clear();
        }
    }

    class Holder extends RecyclerView.ViewHolder {
        @Bind(R.id.tvCityName) TextView tvCityName;
        @Bind(R.id.tvFuzzyNumber) TextView tvFuzzyNumber;
        @Bind(R.id.tvFuelBeforeOrder) TextView tvFuelBeforeOrder;
        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
