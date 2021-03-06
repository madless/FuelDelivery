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
import android.widget.Toast;

import com.codewizards.fueldeliveryapp.R;
import com.codewizards.fueldeliveryapp.entities.City;
import com.codewizards.fueldeliveryapp.entities.Delivery;
import com.codewizards.fueldeliveryapp.entities.FuzzyNumber;
import com.codewizards.fueldeliveryapp.entities.Order;
import com.codewizards.fueldeliveryapp.repository.RepositoryManager;
import com.codewizards.fueldeliveryapp.ui.main.dialog.SpinnerAdapter;
import com.codewizards.fueldeliveryapp.utils.Logger;
import com.codewizards.fueldeliveryapp.utils.calculator.FuzzyNumberHelper;

import java.util.ArrayList;
import java.util.List;
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
    List<City> currentCities;

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
            if(!delivery.getOrders().isEmpty()) {
                List<String> visitedCitiesNames = new ArrayList<>();
                visitedCitiesNames.add(delivery.getSourceCity().getName());
                for(Order order : delivery.getOrders()) {
                    visitedCitiesNames.add(order.getCity().getName());
                }
                //Order lastOrder = delivery.getOrders().get(delivery.getOrders().size() - 1);
                currentCities = new ArrayList<>();
                for(City city : cities) {
                    if(!visitedCitiesNames.contains(city.getName())) {
                        currentCities.add(city);
                    }
//                    if(!city.getName().equals(lastOrder.getCity().getName())) {
//                        currentCities.add(city);
//                    }
                }
            } else {
                currentCities = cities;
            }
            adapter = new SpinnerAdapter(DialogAddOrder.this.getActivity(), R.layout.item_spinner,
                    R.id.tv_item_spinner, currentCities);
            spinnerForCity.setAdapter(adapter);
            spinnerForCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedCity = currentCities.get(i);
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
                if(selectedCity != null) {
                    saveOrder();
                } else {
                    Toast.makeText(getActivity(), "can't save order with empty city", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void saveOrder() {
        FuzzyNumber amountOfFuel = null;
        if(isRandom) {
            amountOfFuel = getRandomFuzzyNumber();
        } else {
            if(isEditTextHasValue(etLeftBorder) && isEditTextHasValue(etMaxValue) &&
                    isEditTextHasValue(etRightBorder)) {
                int x1 = Integer.valueOf(etLeftBorder.getText().toString());
                int x0 = Integer.valueOf(etMaxValue.getText().toString());
                int x2 = Integer.valueOf(etRightBorder.getText().toString());
                if(x1 <= x0 && x0 <= x2) {
                    amountOfFuel = new FuzzyNumber(x1, x0, x2);
                } else {
                    etLeftBorder.setError("Check x1 <= x0 <= x2");
                    return;
                }
            } else {
                return;
            }
        }
        Order order = new Order(delivery.getOrders().size(), selectedCity, amountOfFuel);
        FuzzyNumberHelper.addOrderToList(delivery, order);
        if(listener != null) {
            listener.updateData();
        }
        dismiss();
    }

    private boolean isEditTextHasValue(EditText et) {
        if(et.getText().toString().equals("")) {
            et.setError("Required");
        }
        return !et.getText().toString().equals("");
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
        int maxOrder = 100;
        int maxDiff = 20;
        if(delivery.getOrders().isEmpty()) {
            amountOfFuelLeftX1 = delivery.getAmountOfFuel().getX1();
        } else {
            Order lastOrder = delivery.getOrders().get(delivery.getOrders().size() - 1);
            amountOfFuelLeftX1 = lastOrder.getAmountOfFuelAfterOrder().getX1();
            amountOfFuelLeftX0 = lastOrder.getAmountOfFuelAfterOrder().getX0();
            amountOfFuelLeftX2 = lastOrder.getAmountOfFuelAfterOrder().getX2();
            logger.d("LastOrder: " + lastOrder.getAmountOfFuelAfterOrder().toString());
        }

        int x1 = random.nextInt(maxOrder - minOrder) + minOrder;
        int x0 = random.nextInt(maxOrder + maxDiff - x1) + x1;
        int x2 = random.nextInt(maxOrder + 2 * maxDiff - x0) + x0;
        FuzzyNumber fuzzyNumber = new FuzzyNumber(x1, x0, x2);
        logger.d("New fuzzy: " + fuzzyNumber.toString());
        return fuzzyNumber;
    }
}
