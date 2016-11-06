package com.codewizards.fueldeliveryapp.ui.delivery.tab_graph;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.codewizards.fueldeliveryapp.R;
import com.codewizards.fueldeliveryapp.entities.Delivery;
import com.codewizards.fueldeliveryapp.entities.FuzzyNumber;
import com.codewizards.fueldeliveryapp.entities.Order;
import com.codewizards.fueldeliveryapp.ui.delivery.AddOrderListener;
import com.codewizards.fueldeliveryapp.ui.delivery.BaseTabFragment;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class DeliveryGraphFragment extends BaseTabFragment implements View.OnClickListener, AddOrderListener {
    private GraphView graph;
    private Button btnNext;
    private Button btnPrev;
    private int currentId;
    private Delivery delivery;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_delivery_graph, container, false);
        graph = (GraphView) root.findViewById(R.id.graph);
        btnNext = (Button) root.findViewById(R.id.btn_next);
        btnPrev = (Button) root.findViewById(R.id.btn_prev);
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        delivery = activity.getDelivery();
        if(delivery.getOrders() != null && !delivery.getOrders().isEmpty()) {
            drawGraph();
        } else {
            btnNext.setEnabled(false);
            btnPrev.setEnabled(false);
        }
        if(delivery.getOrders().size() == 1) {
            btnNext.setEnabled(false);
            btnPrev.setEnabled(false);
        }
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(1);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(delivery.getAmountOfFuel().getX2());

        graph.getLegendRenderer().setVisible(true);
    }

    private void drawGraph() {
        graph.removeAllSeries();
        Order order = delivery.getOrders().get(currentId);
        FuzzyNumber amountOfFuel = order.getAmountOfFuel();
        FuzzyNumber amountOfFuelBeforeOrder = order.getAmountOfFuelBeforeOrder();
        drawNumber(Color.RED, amountOfFuel, "A" + (currentId + 1));
        drawNumber(Color.BLUE, amountOfFuelBeforeOrder, "Q" + currentId);
    }

    private void drawNumber(int color, FuzzyNumber number, final String title) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(number.getX1(), 0),
                new DataPoint(number.getX0(), 1),
                new DataPoint(number.getX2(), 0)
        });
        series.setTitle(title);
        series.setColor(color);
        graph.addSeries(series);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                goToNext();
                break;
            case R.id.btn_prev:
                goToPrev();
                break;
        }
    }

    private void goToNext() {
        if(!delivery.getOrders().isEmpty() && delivery.getOrders() != null) {
            int maxId = delivery.getOrders().size() - 1;
            if(currentId < maxId) {
                currentId++;
                if(!btnPrev.isEnabled()) {
                    btnPrev.setEnabled(true);
                }
            }
            if(currentId == maxId) {
                btnNext.setEnabled(false);
            }
            drawGraph();
        }
    }

    private void goToPrev() {
        if(!delivery.getOrders().isEmpty() && delivery.getOrders() != null) {
            if (currentId > 0) {
                currentId--;
                if (!btnNext.isEnabled()) {
                    btnNext.setEnabled(true);
                }
            }
            if (currentId == 0) {
                btnPrev.setEnabled(false);
            }
            drawGraph();
        }
    }

    @Override
    public void processOrderAdding() {
        if(!btnNext.isEnabled() && delivery.getOrders().size() > 1) {
            btnNext.setEnabled(true);
        }
        drawGraph();
    }
}
