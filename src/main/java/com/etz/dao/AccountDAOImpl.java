package com.etz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.etz.exception.AccountCreationException;
import com.etz.exception.AccountNotFoundException;
import com.etz.model.Account;
import com.etz.model.AccountType;
import com.etz.model.CurrentAccount;
import com.etz.model.SavingsAccount;
import com.etz.util.DatabaseConnection;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped 
public class AccountDAOImpl implements AccountDAO {
    
    @Override
    public long create(Account account) {

        String sql = """
                INSERT INTO accounts(user_id, account_type, balance, pin)
                VALUES (?, ?, ?, ?)
                """;
        
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setLong(1, account.getUserId());
            ps.setString(2, account.getAccountType().name());
            ps.setDouble(3, account.getBalance());
            ps.setInt(4, account.getPin()); 

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            
            if (rs.next()) {
                return rs.getLong(1);
            }

            throw new AccountCreationException("Failed to create account");
        } catch (SQLException e) {
            throw new AccountCreationException("Account creation failed", e);
        }
    }

    @Override
    public Account findByAccountNumber(long accountNumber) {

        String sql = """
                SELECT account_number, user_id, account_type, balance, pin
                FROM accounts
                WHERE account_number = ?
                """;
        
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setLong(1, accountNumber);

            ResultSet rs = ps.executeQuery();

            if(!rs.next())
                return null;

            return mapRow(rs);

        } catch (SQLException e) {
            throw new AccountNotFoundException("Account lookup failed");
            
        }
    }

    @Override
    public List<Account> findByUserId(long userId) {

        String sql = """
                SELECT account_number, user_id, account_type, balance, pin
                FROM accounts
                WHERE user_id = ?
                """;
        
        List<Account> accounts = new ArrayList<>();

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                accounts.add(mapRow(rs));
            }

            return accounts;

        } catch (SQLException e) {
            throw new AccountNotFoundException("Accounts lookup failed", e);
        }
    }

    @Override
    public void updateBalance(long accountNumber, double newBalance) {

        String sql = """
                UPDATE accounts
                SET balance = ?
                WHERE account_number = ?
                """;

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setDouble(1, newBalance);
            ps.setLong(2, accountNumber);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Balance update failed", e);
        }
    }

    @Override
    public int getPin(long accountNumber) {

        String sql = "SELECT pin FROM accounts WHERE account_number = ?";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setLong(1, accountNumber);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new RuntimeException("Account not found");
            }

            return rs.getInt("pin");

        } catch (SQLException e) {
            throw new RuntimeException("PIN lookup failed", e);
        }
    }
    
    private Account mapRow(ResultSet rs) throws SQLException {

        AccountType type = AccountType.valueOf(rs.getString("account_type"));

        Account account = 
            (type == AccountType.SAVINGS)
                ? new SavingsAccount()
                : new CurrentAccount();

        account.setAccountNumber(rs.getLong("account_number"));
        account.setUserId(rs.getLong("user_id"));
        account.setAccountType(type);
        account.setBalance(rs.getDouble("balance"));
        account.setPin(rs.getInt("pin"));

        return account;
    }
}
