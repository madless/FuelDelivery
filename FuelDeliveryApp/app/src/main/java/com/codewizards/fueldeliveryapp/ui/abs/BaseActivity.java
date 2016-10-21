package com.codewizards.fueldeliveryapp.ui.abs;

import android.support.v7.app.AppCompatActivity;

import com.codewizards.fueldeliveryapp.utils.Logger;

/**
 * Created by dmikhov on 21.10.2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Logger logger = Logger.getLogger(this.getClass());
}
