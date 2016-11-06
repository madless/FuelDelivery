package com.codewizards.fueldeliveryapp.ui.delivery.tab_details;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.codewizards.fueldeliveryapp.R;
import com.codewizards.fueldeliveryapp.entities.City;
import com.codewizards.fueldeliveryapp.entities.Delivery;
import com.codewizards.fueldeliveryapp.entities.FuzzyNumber;
import com.codewizards.fueldeliveryapp.entities.Order;
import com.codewizards.fueldeliveryapp.repository.RepositoryManager;
import com.codewizards.fueldeliveryapp.ui.main.dialog.SpinnerAdapter;
import com.codewizards.fueldeliveryapp.utils.Logger;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Интернет on 06.11.2016.
 */

public class DialogAddOrder extends DialogFragment implements View.OnClickListener {
    @Bind(R.id.cb_random) CheckBox cbRandom;
    @Bind(R.id.left_border) EditText etLeftBorder;
    @Bind(R.id.right_border) EditText etRightBorder;
    @Bind(R.id.max_value) EditText etMaxValue;
    @Bind(R.id.spinner_for_cities) Spinner spinnerForCity;
    @Bind(R.id.btn_cancel) Button btnCancel;
    @Bind(R.id.btn_save)Button btnSave;
    private City selectedCity;
    private SpinnerAdapter adapter;
    private boolean isRandom;
    private Delivery delivery;
    private UpdateOrdersListener listener;
    Logger logger = Logger.getLogger(this.getClass());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_order, container, false);
        ButterKnife.bind(this, view);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        cbRandom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isRandom = b;
                changeState(!b);
            }
        });
        return view;
    }

    private void changeState(boolean state) {
        etLeftBorder.setEnabled(state);
        etMaxValue.setEnabled(state);
        etRightBorder.setEnabled(state);
        spinnerForCity.setSelected(state);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RepositoryManager.get().getCities().subscribe(cities -> {
            adapter = new SpinnerAdapter(DialogAddOrder.this.getActivity(), R.layout.item_spinner,
                    R.id.tv_item_spinner, cities);
            spinnerForCity.setAdapter(adapter);
            spinnerForCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedCity = cities.get(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_save:
                saveOrder();
                break;
        }
    }

    private void saveOrder() {
        FuzzyNumber amountOfFuel = null;
        if(isRandom) {
            amountOfFuel = getRandomFuzzyNumber();
        } else {
            int x1 = Integer.valueOf(etLeftBorder.getText().toString());
            int x0 = Integer.valueOf(etMaxValue.getText().toString());
            int x2 = Integer.valueOf(etRightBorder.getText().toString());
            amountOfFuel = new FuzzyNumber(x1, x0, x2);
        }
        Order order = new Order(delivery.getOrders().size(), selectedCity, amountOfFuel);
        delivery.getOrders().add(order);
        if(listener != null) {
            listener.updateData();
        }
        dismiss();
    }

    public void attachDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public void setListener(UpdateOrdersListener listener) {
        this.listener = listener;
    }

    private FuzzyNumber getRandomFuzzyNumber() {
        Random random = new Random();
        int amountOfFuelLeftX1 = -1;
        int amountOfFuelLeftX0 = -1;
        int amountOfFuelLeftX2 = -1;
        int minOrder = 50;
        if(delivery.getOrders().isEmpty()) {
            amountOfFuelLeftX1 = delivery.getAmountOfFuel().getX1();
        } else {
            Order lastOrder = delivery.getOrders().get(delivery.getOrders().size() - 1);
            amountOfFuelLeftX1 = lastOrder.getAmountOfFuelAfterOrder().getX1();
            amountOfFuelLeftX0 = lastOrder.getAmountOfFuelAfterOrder().getX0();
            amountOfFuelLeftX2 = lastOrder.getAmountOfFuelAfterOrder().getX2();
            logger.d("LastOrder: " + lastOrder.getAmountOfFuelAfterOrder().toString());
        }

        int x1 = random.nextInt(amountOfFuelLeftX1 - minOrder) + minOrder;
        int x0 = random.nextInt(amountOfFuelLeftX0 - x1) + x1;
        int x2 = random.nextInt(amountOfFuelLeftX2 - x1) + x1;
        FuzzyNumber fuzzyNumber = new FuzzyNumber(x1, x0, x2);
        logger.d("New fuzzy: " + fuzzyNumber.toString());
        return fuzzyNumber;
    }
}
