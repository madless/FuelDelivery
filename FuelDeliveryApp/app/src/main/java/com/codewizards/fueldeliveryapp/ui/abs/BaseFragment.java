package com.codewizards.fueldeliveryapp.ui.abs;

import android.support.v4.app.Fragment;

import com.codewizards.fueldeliveryapp.utils.Logger;

/**
 * Created by dmikhov on 21.10.2016.
 */
public abstract class BaseFragment extends Fragment {
    protected Logger logger = Logger.getLogger(this.getClass());
}
