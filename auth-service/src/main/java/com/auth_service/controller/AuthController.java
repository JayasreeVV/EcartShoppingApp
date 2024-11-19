package com.auth_service.controller;

import com.auth_service.dto.UserDto;
import com.auth_service.service.AuthService;
import com.auth_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/register")
    public String saveUser(@RequestBody UserDto userDto){
        return authService.saveUserCred(userDto);
    }

    @PostMapping("/generate/token")
    public String generateToken(@RequestBody UserDto userDto) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getName(), userDto.getPassword()));
        if (authenticate.isAuthenticated()) {
            return jwtUtil.generateToken(userDto.getName());
        } else {
            throw new BadCredentialsException("Username and password does not match");
        }
    }

    @GetMapping("/validate")
    public boolean validateToken(@RequestParam("token") String token) {
        return jwtUtil.isTokenValid(token);
    }
}
