package com.codewizards.fueldeliveryapp.ui.main.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.codewizards.fueldeliveryapp.R;
import com.codewizards.fueldeliveryapp.entities.City;
import com.codewizards.fueldeliveryapp.entities.Delivery;
import com.codewizards.fueldeliveryapp.entities.FuzzyNumber;
import com.codewizards.fueldeliveryapp.repository.RepositoryManager;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Интернет on 05.11.2016.
 */

public class DialogAddDelivery extends DialogFragment implements View.OnClickListener{
    private EditText etAmountOfFuel;
    private Spinner spinnerForCity;
    private SpinnerAdapter adapter;
    private Button btnCancel;
    private Button btnSave;
    private City selectedCity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_delivery, container, false);
        etAmountOfFuel = (EditText) view.findViewById(R.id.et_amount_of_fuel);
        spinnerForCity = (Spinner) view.findViewById(R.id.spinner_for_cities);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        btnSave = (Button) view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RepositoryManager.get().getCities().subscribe(cities -> {
            adapter = new SpinnerAdapter(DialogAddDelivery.this.getActivity(), R.layout.item_spinner,
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
            case R.id.btn_save:
                saveDelivery();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    private void saveDelivery() {
        if(etAmountOfFuel.getText().toString().equals("")) {
            etAmountOfFuel.setError("Required");
            return;
        }
        int amountOfFuel = Integer.valueOf(etAmountOfFuel.getText().toString());
        FuzzyNumber amountOfFuelFuzzy = new FuzzyNumber(amountOfFuel);
        Delivery delivery = new Delivery(selectedCity, amountOfFuelFuzzy);
        RepositoryManager.get().addDelivery(delivery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(deliveries -> {
            RepositoryManager.get().updateDeliveries(deliveries);
        });
    }
}
