package com.codewizards.fueldeliveryapp.ui.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codewizards.fueldeliveryapp.R;
import com.codewizards.fueldeliveryapp.entities.Delivery;
import com.codewizards.fueldeliveryapp.ui.abs.BaseActivity;
import com.codewizards.fueldeliveryapp.ui.main.dialog.DialogAddDelivery;
import com.codewizards.fueldeliveryapp.ui.mvp.PresentersCache;
import com.codewizards.fueldeliveryapp.ui.mvp.PresentersFactory;
import com.codewizards.fueldeliveryapp.utils.Const;
import com.codewizards.fueldeliveryapp.utils.IntentHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class MainActivity extends BaseActivity {
    @Bind(R.id.swipeRefresh) SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.fabAdd) FloatingActionButton fabAdd;
    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.rvDeliveries) RecyclerView rvDeliveries;
    @Bind(R.id.tvNoData) TextView tvNoData;

    PresentersFactory<MainActivityPresenter> presentersFactory = MainActivityPresenter::new;
    MainActivityPresenter presenter;
    DeliveriesAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = PresentersCache.get().getPresenter(Const.PRESENTER_MAIN_ACTIVITY, presentersFactory);
        init();

        presenter.attachView(this);
        presenter.onCreate();
    }

    public void init() {
        adapter = new DeliveriesAdapter();
        rvDeliveries.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvDeliveries.setAdapter(adapter);
        adapter.setOnDeliveryClickListener(delivery -> {
            IntentHelper.openDeliveryActivity(this, delivery.getId());
        });
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAddDelivery dialogAddDelivery = new DialogAddDelivery();
                dialogAddDelivery.show(getFragmentManager(), "");
            }
        });
        swipeRefresh.setOnRefreshListener(() -> {
            swipeRefresh.setRefreshing(true);
            presenter.updateData();
        });
    }

    public void onLoadingStarted() {
        logger.d("onLoadingStarted()");
        progressBar.setVisibility(View.VISIBLE);
        rvDeliveries.setVisibility(View.GONE);
        tvNoData.setVisibility(View.GONE);
    }

    public void onLoadingFinishedSuccessfully(List<Delivery> deliveries) {
        logger.d("onLoadingFinishedSuccessfully");
        swipeRefresh.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
        rvDeliveries.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.GONE);

        adapter.setData(deliveries);
        adapter.notifyDataSetChanged();
    }

    public void onLoadingFinishedUnsuccessfully() {
        logger.d("onLoadingFinishedUnsuccessfully()");
        swipeRefresh.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
        rvDeliveries.setVisibility(View.GONE);
        tvNoData.setVisibility(View.VISIBLE);

        adapter.clearData();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isChangingConfigurations() && presenter != null) {
            presenter.onTotalDestroy();
            PresentersCache.get().removePresenter(Const.PRESENTER_MAIN_ACTIVITY);
        }
    }
}
