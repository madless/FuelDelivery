package com.codewizards.fueldeliveryapp.entities;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class FuzzyNumber {
    private int x1;
    private int x0; // middle
    private int x2;

    public FuzzyNumber(int x1, int x0, int x2) {
        this.x1 = x1;
        this.x0 = x0;
        this.x2 = x2;
    }

    @Override
    public String toString() {
        return "(" + x1 + ":" + x0 + ":" + x2 + ") ";
    }
}
