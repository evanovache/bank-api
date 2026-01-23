package com.etz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.etz.exception.AccountCreationException;
import com.etz.model.User;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class UserDAOImpl implements UserDAO {

    @Resource(lookup = "java:/jdbc/ApexBankDS2")
    private DataSource dataSource;
    
    @Override
    public long create(User user) {
        
        String sql = """
                INSERT INTO users (full_name, email, password)
                VALUES (?, ?, ?)
                """;

        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getLong(1);
            }

            throw new AccountCreationException("Failed to create user");
        } catch (SQLException e) {
            throw new AccountCreationException("user creation failed", e);
        }
    }

    @Override
    public User findById(long userId) {

        String sql = """
                SELECT user_id, full_name, email, password, created_at
                FROM users
                WHERE user_id = ?
                """;
        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next())
                return null;

            return mapRow(rs);

        } catch (SQLException e) {
            throw new RuntimeException("User lookup failed", e);
        }
    }

    @Override
    public User findByEmail(String email) {

        String sql = """
                SELECT user_id, full_name, email, password, created_at
                FROM users
                WHERE email = ?
                """;
        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) 
                return null;

            return mapRow(rs);

        } catch (SQLException e) {
            throw new RuntimeException("User lookup failed", e);
        }
    }

    @Override
    public boolean existsByEmail (String email) {

        String sql = "SELECT 1 FROM users WHERE email = ?";

        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Email check failed", e);
        }
    }
    
    private User mapRow(ResultSet rs) throws SQLException {

        User user = new User();
        user.setUserId(rs.getLong("user_id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return user;
    }
}
