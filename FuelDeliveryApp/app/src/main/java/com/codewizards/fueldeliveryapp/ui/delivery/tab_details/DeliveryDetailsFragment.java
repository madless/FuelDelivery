package com.codewizards.fueldeliveryapp.ui.delivery.tab_details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codewizards.fueldeliveryapp.R;
import com.codewizards.fueldeliveryapp.entities.Delivery;
import com.codewizards.fueldeliveryapp.entities.Order;
import com.codewizards.fueldeliveryapp.ui.delivery.BaseTabFragment;
import com.codewizards.fueldeliveryapp.utils.calculator.FuzzyNumberHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class DeliveryDetailsFragment extends BaseTabFragment {

    @Bind(R.id.tvDeliveryName) TextView tvDeliveryName;
    @Bind(R.id.tvAmountOfFuelAtBeginning) TextView tvAmountOfFuelAtBeginning;
    @Bind(R.id.tvAmountOfFuelLeft) TextView tvAmountOfFuelLeft;
    @Bind(R.id.rvOrders) RecyclerView rvOrders;
    @Bind(R.id.fabAdd) FloatingActionButton fabAdd;
    private OrdersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_delivery_details, container, false);
        ButterKnife.bind(this, root);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAddOrder dialogAddOrder = new DialogAddOrder();
                dialogAddOrder.show(getActivity().getFragmentManager(), "");
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    public void initView() {
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new OrdersAdapter();
        rvOrders.setAdapter(adapter);
    }

    public void initData() {
        if(activity != null) {
            Delivery delivery = activity.getDelivery();
            FuzzyNumberHelper.calculateListOfOrders(delivery);
            List<Order> orders = delivery.getOrders();
            if(orders != null && !orders.isEmpty()) {
                adapter.setData(orders);
                adapter.notifyDataSetChanged();
                tvAmountOfFuelAtBeginning.setText(String.format(getString(R.string.amount_of_fuel_at_beginning), orders.get(0).getAmountOfFuelBeforeOrder().toString()));
                tvAmountOfFuelLeft.setText(String.format(getString(R.string.amount_of_fuel_left), orders.get(orders.size() - 1).getAmountOfFuelAfterOrder().toString()));
            } else {
                tvAmountOfFuelAtBeginning.setText(String.format(getString(R.string.amount_of_fuel_at_beginning), delivery.getAmountOfFuel().toString()));
                tvAmountOfFuelLeft.setText(String.format(getString(R.string.amount_of_fuel_left), delivery.getAmountOfFuel().toString()));
            }
            tvDeliveryName.setText(delivery.getName());
        } else {
            logger.w("activity == null");
        }
    }
}
