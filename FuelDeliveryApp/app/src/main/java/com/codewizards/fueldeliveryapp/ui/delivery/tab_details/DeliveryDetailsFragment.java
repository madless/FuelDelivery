package com.codewizards.fueldeliveryapp.ui.delivery.tab_details;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class DeliveryDetailsFragment extends BaseTabFragment {

    @Bind(R.id.tvDeliveryName) TextView tvDeliveryName;
    @Bind(R.id.rvOrders) RecyclerView rvOrders;
    private OrdersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_delivery_details, container, false);
        ButterKnife.bind(this, root);
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
            List<Order> orders = delivery.getOrders();
            adapter.setData(orders);
            adapter.notifyDataSetChanged();
            tvDeliveryName.setText(delivery.getName());
        } else {
            logger.w("activity == null");
        }
    }
}
