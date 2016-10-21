package com.codewizards.fueldeliveryapp.ui.mvp;

import android.support.annotation.NonNull;

import com.codewizards.fueldeliveryapp.ui.abs.BasePresenter;

/**
 * Created by dmikhov on 21.10.2016.
 */
public interface PresentersFactory<T extends BasePresenter> {
    /**
     * Create a new instance of a Presenter
     *
     * @return The Presenter instance
     */
    @NonNull
    T createPresenter();
}
