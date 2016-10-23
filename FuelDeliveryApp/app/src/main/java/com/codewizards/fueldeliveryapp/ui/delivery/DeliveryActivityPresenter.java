package com.codewizards.fueldeliveryapp.ui.delivery;

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
public class DeliveryActivityPresenter extends BasePresenter {
    private DeliveryActivity view;
    private List<Delivery> deliveries;
    private boolean isDataFetched;
    private Subscription subscription;
    private int selectedDeliveryId;
    private Delivery selectedDelivery;

    public void attachView(DeliveryActivity view) {
        this.view = view;
    }

    public void onCreate(int selectedDeliveryId) {
        logger.d("onCreate(), selectedDeliveryId: " + selectedDeliveryId);
        if(view == null) {
            logger.error("view == null, attach view first!");
        }
        this.selectedDeliveryId = selectedDeliveryId;
        if(!isDataFetched) {
            view.onLoadingStarted();
            fetchData();
        } else {
            if(deliveries != null && !deliveries.isEmpty()) {
                onDataFetchedSuccessfully(deliveries);
            } else {
                onDataFetchedUnsuccessfully();
            }
        }
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
        selectedDelivery = null;
        for (Delivery temp: deliveries) {
            if(temp.getId() == selectedDeliveryId) {
                selectedDelivery = temp;
                break;
            }
        }
        if(selectedDelivery != null) {
            view.onLoadingFinishedSuccessfully(selectedDelivery);
        } else {
            view.onLoadingFinishedUnsuccessfully();
        }
    }

    private void onDataFetchedUnsuccessfully() {
        view.onLoadingFinishedUnsuccessfully();
    }

    public void onTotalDestroy() {
        logger.d("onTotalDestroy()");
        if(subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public Delivery getSelectedDelivery() {
        return selectedDelivery;
    }
}
