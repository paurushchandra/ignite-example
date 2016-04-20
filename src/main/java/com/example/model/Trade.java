package com.example.model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by user on 19/4/16.
 */
public class Trade implements Serializable {

    private String sedol;
    private double price;
    private int quantity;
    private double amount;
    private Strategy strategy;
    private Side side;
    private Calendar executionDate;
    private Calendar settlementDate;

    public String getSedol() {
        return sedol;
    }

    public void setSedol(String sedol) {
        this.sedol = sedol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public Calendar getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Calendar executionDate) {
        this.executionDate = executionDate;
    }

    public Calendar getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Calendar settlementDate) {
        this.settlementDate = settlementDate;
    }
}
