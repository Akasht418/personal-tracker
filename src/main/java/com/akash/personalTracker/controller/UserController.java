package com.akash.personalTracker.controller;

import com.akash.personalTracker.entity.User;
import com.akash.personalTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    UserRepository userRepository;

    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @PostMapping
    public User createUser(@RequestBody User user){
        return userRepository.save(user);
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
