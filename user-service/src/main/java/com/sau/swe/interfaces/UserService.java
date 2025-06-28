package com.sau.swe.interfaces;


import com.sau.swe.entity.Users;

import java.util.List;

public interface UserService {
    Users getUserById(Long id);

    List<Users> getAllUsers();
}
