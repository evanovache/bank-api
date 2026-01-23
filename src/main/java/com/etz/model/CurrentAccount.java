package com.etz.model;

import com.etz.exception.InsufficientFundsException;

public class CurrentAccount extends Account {
    
    private static final double OVERDRAFT_LIMIT = -500.0;

    @Override
    public void validateWithdrawal(double amount) {
        if (getBalance() - amount < OVERDRAFT_LIMIT) {
            throw new InsufficientFundsException (
                "You do not have sufficient funds or available overdraft to complete this withdrawal."
            );
        }    
    }
}