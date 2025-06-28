package com.sau.swe.service;

import com.sau.swe.dao.UserRepository;
import com.sau.swe.entity.Users;
import com.sau.swe.exceptions.UserNotFoundException;
import com.sau.swe.interfaces.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public Users getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
}
