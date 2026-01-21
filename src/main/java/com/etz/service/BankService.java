package com.etz.service;

import java.time.LocalDateTime;
import java.util.List;

import com.etz.dao.AccountDAO;
import com.etz.dao.TransactionDAO;
import com.etz.exception.AccountNotFoundException;
import com.etz.exception.InvalidPinException;
import com.etz.model.Account;
import com.etz.model.Transaction;
import com.etz.model.TransactionType;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class BankService {

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private TransactionDAO transactionDAO;
    

    public double getBalance(long accountNumber, int pin) {

        Account account = getAccount(accountNumber);
        validatePin(account, pin);

        return account.getBalance();
    }


    public void deposit(long accountNumber, int pin, double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        Account account = getAccount(accountNumber);
        validatePin(account, pin);

        double newBalance = account.getBalance() + amount;
        accountDAO.updateBalance(accountNumber, newBalance);

        recordTransaction(accountNumber, amount, TransactionType.DEPOSIT);
    }


    public void withdraw(long accountNumber, int pin, double amount) {

        if (amount <= 0)
            throw new IllegalArgumentException("Withdrawal amount must be positive");

        Account account = getAccount(accountNumber);
        validatePin(account, pin);

        account.validateWithdrawal(amount);

        double newBalance = account.getBalance() - amount;
        accountDAO.updateBalance(accountNumber, newBalance);

        recordTransaction(accountNumber, amount, TransactionType.WITHDRAWAL);
    }


    public List<Transaction> getRecentTransactions(long accountNumber, int pin, int limit) {

            Account account = getAccount(accountNumber);
            validatePin(account, pin);

            return transactionDAO.findRecent(accountNumber, limit);
    }



    private Account getAccount(long accountNumber) {
        Account account = accountDAO.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account not found");
        }
        return account;
    }


    private void validatePin(Account account, int pin) {
    if (account.getPin() != pin) {
        throw new InvalidPinException("Invalid PIN");
        }
    }


    private void recordTransaction(
        long accountNumber,
        double amount, 
        TransactionType type
    ) {
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(amount);
        transaction.setTransactionType(type);
        transaction.setTimeOfTransaction(LocalDateTime.now());

        transactionDAO.save(transaction);
    }
}

