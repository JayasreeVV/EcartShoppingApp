package com.auth_service.service;

import com.auth_service.dto.UserDto;
import com.auth_service.entity.UserCredential;
import com.auth_service.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String saveUserCred(UserDto userDto){
        userCredentialRepository.save(userCredentialMapper(userDto));
        return "User credentials saved successfully";
    }

    public UserCredential userCredentialMapper(UserDto userDto){
        UserCredential userCredential = new UserCredential();
        userCredential.setName(userDto.getName());
        userCredential.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userCredential.setEmail(userDto.getEmail());
        return userCredential;
    }
}
