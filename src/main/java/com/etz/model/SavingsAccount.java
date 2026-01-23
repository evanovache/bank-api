package com.etz.model;

import com.etz.exception.InsufficientFundsException;

public class SavingsAccount extends Account {
    
    private static final double MINIMUM_BALANCE = 50.0;
    private static final double INTEREST_RATE = 0.05;

    @Override
    public void validateWithdrawal(double amount) {
        if (getBalance() - amount < MINIMUM_BALANCE) {
            throw new InsufficientFundsException (
                "You do not have sufficient funds to complete this withdrawal"
            );
        }
    }

    public double calculateInterest() {
        return getBalance() * INTEREST_RATE;
    } 
} 
