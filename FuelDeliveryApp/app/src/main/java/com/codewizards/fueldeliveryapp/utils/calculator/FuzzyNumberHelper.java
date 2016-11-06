package com.codewizards.fueldeliveryapp.utils.calculator;

import com.codewizards.fueldeliveryapp.entities.Delivery;
import com.codewizards.fueldeliveryapp.entities.FuzzyNumber;
import com.codewizards.fueldeliveryapp.entities.Order;

import java.util.List;

/**
 * Created by Интернет on 06.11.2016.
 */

public class FuzzyNumberHelper {
    public static void calculateListOfOrders(Delivery delivery) {
        if(delivery.getOrders() != null && !delivery.getOrders().isEmpty()) {
            FuzzyNumber amountOfFuel = delivery.getAmountOfFuel();
            Order firstOrder = delivery.getOrders().get(0);
            if (firstOrder != null) {
                proccessOrder(firstOrder, amountOfFuel);
            }
            List<Order> orders = delivery.getOrders();
            for (int i = 1; i < orders.size(); i++) {
                proccessOrder(orders.get(i), orders.get(i - 1).getAmountOfFuelAfterOrder());
            }
        }
    }

    public static void addOrderToList(Delivery delivery, Order order) {
        List<Order> orders = delivery.getOrders();
        if(!orders.isEmpty()) {
            Order lastOrder = orders.get(orders.size() - 1);
            proccessOrder(order, lastOrder.getAmountOfFuelAfterOrder());
        } else {
            FuzzyNumber amountOfFuel = delivery.getAmountOfFuel();
            proccessOrder(order, amountOfFuel);
        }
        orders.add(order);
    }

    private static void proccessOrder(Order order, FuzzyNumber amountOfFuelBeforeOrder) {
        order.setAmountOfFuelBeforeOrder(amountOfFuelBeforeOrder);
        order.setAmountOfFuelAfterOrder(subFuzzyNumbers(amountOfFuelBeforeOrder,
                order.getAmountOfFuel()));
    }

    private static FuzzyNumber subFuzzyNumbers(FuzzyNumber a, FuzzyNumber b) {
        int leftBorder = a.getX1() - b.getX2();
        int rightBorder = a.getX2() - b.getX1();
        int max = a.getX0() - b.getX0();
        return new FuzzyNumber(leftBorder, max, rightBorder);
    }

    public static boolean isFuzzyValid(FuzzyNumber fuzzyNumber) {
        return fuzzyNumber.getX1() > 0 && fuzzyNumber.getX1() <= fuzzyNumber.getX0() && fuzzyNumber.getX0() <= fuzzyNumber.getX2();
    }

}
