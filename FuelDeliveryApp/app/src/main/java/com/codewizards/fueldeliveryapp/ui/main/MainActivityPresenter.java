package com.codewizards.fueldeliveryapp.ui.main;

import com.codewizards.fueldeliveryapp.entities.Delivery;
import com.codewizards.fueldeliveryapp.repository.RepositoryManager;
import com.codewizards.fueldeliveryapp.ui.abs.BasePresenter;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class MainActivityPresenter extends BasePresenter implements OnDeliveriesUpdatedListener {
    private MainActivity view;
    private List<Delivery> deliveries;
    private boolean isDataFetched;
    private Subscription subscription;

    public void attachView(MainActivity view) {
        this.view = view;
    }

    public void onCreate() {
        logger.d("onCreate()");
        if(view == null) {
            logger.error("view == null, attach view first!");
        }
        if(!isDataFetched) {
            RepositoryManager.get().addOnDeliveriesUpdatedListener(this);
            view.onLoadingStarted();
            fetchData();
        } else {
            if(deliveries != null && !deliveries.isEmpty()) {
                view.onLoadingFinishedSuccessfully(deliveries);
            } else {
                view.onLoadingFinishedUnsuccessfully();
            }
        }
    }

    public void updateData() {
        logger.d("updateData()");
        isDataFetched = false;
        fetchData();
    }

    private void fetchData() {
        if(subscription == null || subscription.isUnsubscribed()) {
            subscription = RepositoryManager.get().getDeliveries()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(fetchedDeliveries -> {
                        logger.i("Fetched deliveries: " + fetchedDeliveries);
                        isDataFetched = true;
                        deliveries = fetchedDeliveries;
                        if (deliveries != null && !deliveries.isEmpty()) {
                            onDataFetchedSuccessfully(deliveries);
                        } else {
                            onDataFetchedUnsuccessfully();
                        }
                    }, throwable -> {
                        onDataFetchedUnsuccessfully();
                    });
        } else {
            logger.w("Is already subscribed!");
        }
    }

    private void onDataFetchedSuccessfully(List<Delivery> deliveries) {
        view.onLoadingFinishedSuccessfully(deliveries);
    }

    private void onDataFetchedUnsuccessfully() {
        view.onLoadingFinishedUnsuccessfully();
    }

    public void onTotalDestroy() {
        logger.d("onTotalDestroy()");
        if(subscription != null && !subscription.isUnsubscribed()) {
            RepositoryManager.get().removeOnDeliveriesUpdatedListener(this);
            subscription.unsubscribe();
        }
    }

    @Override
    public void onDeliveriesUpdated(List<Delivery> deliveries) {
        logger.d("onDeliveriesUpdated: " + deliveries);
        onDataFetchedSuccessfully(deliveries);
    }
}
