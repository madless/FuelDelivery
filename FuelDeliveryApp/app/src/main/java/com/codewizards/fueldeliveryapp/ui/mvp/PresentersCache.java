package com.codewizards.fueldeliveryapp.ui.mvp;

import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import com.codewizards.fueldeliveryapp.ui.abs.BasePresenter;
import com.codewizards.fueldeliveryapp.utils.Logger;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class PresentersCache {
    Logger logger = Logger.getLogger(PresentersCache.class);
    private static PresentersCache instance = null;

    private SimpleArrayMap<String, BasePresenter> presenters;

    private PresentersCache() {}

    public static PresentersCache get() {
        if (instance == null) {
            instance = new PresentersCache();
        }
        return instance;
    }

    @SuppressWarnings("unchecked") // Handled internally
    public final <T extends BasePresenter> T getPresenter(
            String who, PresentersFactory<T> presenterFactory) {
        if (presenters == null) {
            presenters = new SimpleArrayMap<>();
        }
        T p = null;
        try {
            p = (T) presenters.get(who);
        } catch (ClassCastException e) {
            Log.w("PresenterActivity", "Duplicate Presenter " +
                    "tag identified: " + who + ". This could " +
                    "cause issues with state.");
        }
        if (p == null) {
            p = presenterFactory.createPresenter();
            presenters.put(who, p);
        }
        return p;
    }

    public final void removePresenter(String who) {
        if (presenters != null) {
            BasePresenter presenter = presenters.remove(who);
            if(presenter != null) {
                logger.d("Presenter " + who + " has removed successfully!");
            } else {
                logger.d("Presenter " + who + " has not removed!");
            }
        }
    }


}
