package com.sau.swe.service;

import com.sau.swe.entity.User;
import com.sau.swe.exceptions.UserNotFoundException;
import com.sau.swe.interfaces.UserService;
import com.sau.swe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
