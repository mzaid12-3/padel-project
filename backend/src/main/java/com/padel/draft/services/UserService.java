package com.padel.draft.services;

import com.padel.draft.domain.user.User;
import com.padel.draft.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }
        return userRepository.save(user);
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
