package com.etz.controller.response;

public class BalanceResponse {
    
    private double balance;

    public BalanceResponse(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }
}