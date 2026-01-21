package com.etz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.etz.model.Transaction;
import com.etz.model.TransactionType;
import com.etz.util.DatabaseConnection;

import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped 
public class TransactionDAOImpl implements TransactionDAO {
    
    @Override
    public void save(Transaction transaction) {

        String sql = """
                INSERT INTO transactions
                (account_number, amount, transaction_type, time_of_transaction)
                VALUES (?, ?, ?, ?)
                """;
        
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setLong(1, transaction.getAccountNumber());
            ps.setDouble(2, transaction.getAmount());
            ps.setString(3, transaction.getTransactionType().name());
            ps.setTimestamp(4, Timestamp.valueOf(transaction.getTimeOfTransaction()));

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Transaction save failed", e);
        }
    }

    @Override
    public List<Transaction> findByAccountNumber(long accountNumber) {
        
        String sql = """
                SELECT transaction_id, account_number, amount, transaction_type, time_of_transaction
                FROM transactions
                WHERE account_number = ?
                ORDER BY time_of_transactions DESC
                """;

        List<Transaction> transactions = new ArrayList<>();

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {

            ps.setLong(1, accountNumber);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                transactions.add(mapRow(rs));
            }

            return transactions;

        } catch (SQLException e) {
            throw new RuntimeException("Transaction lookup failed", e);
        }
    }

    @Override
    public List<Transaction> findRecent(long accountNumber, int limit) {

        String sql = """
                SELECT transaction_id, account_number, amount, transaction_type, time_of_transaction
                FROM transactions
                WHERE account_number = ?
                ORDER BY time_of_transaction DESC
                LIMIT ?
                """;
            
        List<Transaction> transactions = new ArrayList<>();

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setLong(1, accountNumber);
            ps.setInt(2, limit);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                transactions.add(mapRow(rs));
            }

            return transactions;

        } catch (SQLException e) {
            throw new RuntimeException("Recent transactions lookup failed", e);
        }
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {

        Transaction transaction = new Transaction();

        transaction.setTransactionId(rs.getLong("transaction_id"));
        transaction.setAccountNumber(rs.getLong("account_number"));
        transaction.setAmount(rs.getDouble("amount"));
        transaction.setTransactionType(
            TransactionType.valueOf(rs.getString("transaction_type"))
        );
        transaction.setTimeOfTransaction(
            rs.getTimestamp("time_of_transaction").toLocalDateTime()
        );

        return transaction;
    }
}
