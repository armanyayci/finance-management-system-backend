package com.sau.swe.service;

import com.sau.swe.dao.UserRepository;
import com.sau.swe.entity.Users;
import com.sau.swe.exceptions.UserNotFoundException;
import com.sau.swe.interfaces.UserService;

import com.sau.swe.utils.exception.GenericFinanceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
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

    @Override
    @Transactional
    public void addImage(Long userId, MultipartFile file) {
        Users user = userRepository.findById(userId).orElseThrow(
                ()-> new GenericFinanceException("generic.auth.userNotFound"));
        try {
            user.setImage(file.getBytes());
        } catch (IOException e) {
            throw new GenericFinanceException("user.profile.add.image.fail");
        }
        userRepository.save(user);
        log.info("image saved successfully, userId: {}", userId);
    }
}
