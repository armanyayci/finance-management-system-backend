package com.sau.swe.interfaces;

import com.sau.swe.entity.User;
import java.util.List;

public interface UserService {
    User getUserById(Long id);

    List<User> getAllUsers();
}
