package com.sau.swe.interfaces;


import com.sau.swe.entity.Users;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    Users getUserById(Long id);

    List<Users> getAllUsers();

    void addImage(Long userId, MultipartFile file);
}
