package com.codewizards.fueldeliveryapp.ui.main.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.codewizards.fueldeliveryapp.R;
import com.codewizards.fueldeliveryapp.entities.City;
import com.codewizards.fueldeliveryapp.repository.RepositoryManager;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Интернет on 05.11.2016.
 */

public class DialogAddDelivery extends DialogFragment {
    private EditText etAmountOfFuel;
    private Spinner spinnerForCity;
    private SpinnerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_delivery, container, false);
        etAmountOfFuel = (EditText) view.findViewById(R.id.et_amount_of_fuel);
        spinnerForCity = (Spinner) view.findViewById(R.id.spinner_for_cities);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RepositoryManager.get().getCities().subscribe(cities -> {
            adapter = new SpinnerAdapter(DialogAddDelivery.this.getActivity(), R.layout.item_spinner,
                    R.id.tv_item_spinner, cities);
            spinnerForCity.setAdapter(adapter);
        });

    }
}
