package com.codewizards.fueldeliveryapp.ui.delivery;

import com.codewizards.fueldeliveryapp.entities.Delivery;
import com.codewizards.fueldeliveryapp.ui.abs.BaseFragment;

/**
 * Created by dmikhov on 21.10.2016.
 */
public abstract class BaseTabFragment extends BaseFragment {
    protected Delivery delivery;
    public void setData(Delivery delivery) {
        this.delivery = delivery;
    }
}
