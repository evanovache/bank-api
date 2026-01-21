package com.etz.dao;

import java.util.List;

import com.etz.model.Transaction;

public interface TransactionDAO {
    
    void save(Transaction transaction);
    
    List<Transaction> findByAccountNumber(long accountNumber);

    List<Transaction> findRecent(long accountNumber, int limit);
}
