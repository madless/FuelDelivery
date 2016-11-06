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
import com.codewizards.fueldeliveryapp.repository.RepositoryManager;
import com.codewizards.fueldeliveryapp.ui.main.dialog.DialogAddDelivery;
import com.codewizards.fueldeliveryapp.ui.main.dialog.SpinnerAdapter;

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

    }
}
