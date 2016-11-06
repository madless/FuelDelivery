package com.codewizards.fueldeliveryapp.ui.delivery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codewizards.fueldeliveryapp.R;
import com.codewizards.fueldeliveryapp.entities.Delivery;
import com.codewizards.fueldeliveryapp.ui.abs.BaseActivity;
import com.codewizards.fueldeliveryapp.ui.adapters.SimplePagerAdapter;
import com.codewizards.fueldeliveryapp.ui.delivery.tab_details.DeliveryDetailsFragment;
import com.codewizards.fueldeliveryapp.ui.delivery.tab_graph.DeliveryGraphFragment;
import com.codewizards.fueldeliveryapp.ui.delivery.tab_map.DeliveryMapFragment;
import com.codewizards.fueldeliveryapp.ui.mvp.PresentersCache;
import com.codewizards.fueldeliveryapp.ui.mvp.PresentersFactory;
import com.codewizards.fueldeliveryapp.utils.Const;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class DeliveryActivity extends BaseActivity {

    @Bind(R.id.tvInvalidData) TextView tvInvalidData;
    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.viewPager) ViewPager viewPager;
    @Bind(R.id.tabs) TabLayout tabs;

    private List<BaseTabFragment> fragments = new ArrayList<>();
    private SimplePagerAdapter adapter;
    private PresentersFactory<DeliveryActivityPresenter> factory = DeliveryActivityPresenter::new;
    private DeliveryActivityPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        ButterKnife.bind(this);
        presenter = PresentersCache.get().getPresenter(Const.PRESENTER_DELIVERY_ACTIVITY, factory);

        int deliveryId = getIntent().getIntExtra(Const.EXTRA_DELIVERY_ID, -1);
        presenter.attachView(this);
        presenter.onCreate(deliveryId);
    }

    public void createFragments() {
        logger.d("create fragments");
        BaseTabFragment fragmentDetails = new DeliveryDetailsFragment();
        BaseTabFragment fragmentGraph = new DeliveryGraphFragment();
        ((DeliveryDetailsFragment)fragmentDetails).setListener((AddOrderListener) fragmentGraph);
        BaseTabFragment fragmentMap = new DeliveryMapFragment();
        fragments.clear();
        fragments.add(fragmentDetails);
        fragments.add(fragmentGraph);
        fragments.add(fragmentMap);
    }

    public void initTabs() {
        logger.d("init");
        List<String> tabTitles = new ArrayList<>();
        String tabDetails = "Details";
        String tabGraph = "Graph";
        String tabMap = "Map";
        tabTitles.add(tabDetails);
        tabTitles.add(tabGraph);
        tabTitles.add(tabMap);
        adapter = new SimplePagerAdapter(getSupportFragmentManager(), fragments, tabTitles);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        tabs.setupWithViewPager(viewPager);
    }

    public void onLoadingStarted() {
        logger.d("onLoadingStarted()");
        progressBar.setVisibility(View.VISIBLE);
        tvInvalidData.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        tabs.setVisibility(View.GONE);
    }

    public void onLoadingFinishedSuccessfully(Delivery delivery) {
        logger.d("onLoadingFinishedSuccessfully");
        progressBar.setVisibility(View.GONE);
        tvInvalidData.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        tabs.setVisibility(View.VISIBLE);
        createFragments();
//        for (BaseTabFragment fragment: fragments) {
//            if(fragment != null) {
//                fragment.setData(delivery);
//            } else {
//                logger.w("fragment is null!");
//            }
//        }
        initTabs();
        tabs.invalidate();
    }

    public void onLoadingFinishedUnsuccessfully() {
        logger.d("onLoadingFinishedUnsuccessfully()");
        progressBar.setVisibility(View.GONE);
        tvInvalidData.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        tabs.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    public Delivery getDelivery() {
        if(presenter != null) {
            return presenter.getSelectedDelivery();
        } else {
            logger.w("presenter == null");
            return null;
        }
    }
}
