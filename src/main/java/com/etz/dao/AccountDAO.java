package com.etz.dao;

import java.util.List;

import com.etz.model.Account;


public interface AccountDAO {
    
    long create(Account account);

    Account findByAccountNumber(long accountNumber);

    List<Account> findByUserId(long userId);

    void updateBalance(long accountNumber, double newBalance);

}
