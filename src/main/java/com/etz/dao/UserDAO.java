package com.etz.dao;

import com.etz.model.User;

public interface UserDAO {

    long create(User user);
    
    User findById(long userId);

    User findByEmail(String email);

    boolean existsByEmail(String email);
}
