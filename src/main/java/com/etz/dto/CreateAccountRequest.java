package com.etz.dto;

import com.etz.model.AccountType;

public class CreateAccountRequest {
    
    private AccountType type;
    private double initialDeposit;
    private int pin;
    
    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public double getInitialDeposit() {
        return initialDeposit;
    }

    public void setInitialDeposit(double intialDeposit) {
        this.initialDeposit = intialDeposit;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }
    
}
