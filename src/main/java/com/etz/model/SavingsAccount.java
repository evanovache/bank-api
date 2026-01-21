package com.etz.model;

import com.etz.exception.InsufficientFundsException;

public class SavingsAccount extends Account {
    
    private static final double MINIMUM_BALANCE = 50.0;
    private static final double INTEREST_RATE = 0.05;

    @Override
    public void validateWithdrawal(double amount) {
        if (getBalance() - amount < MINIMUM_BALANCE) {
            throw new InsufficientFundsException (
                "Withdrawal Unsuccessful. Savings account must maintain minimum balance"
            );
        }
    }

    public double calculateInterest() {
        return getBalance() * INTEREST_RATE;
    } 
} 
