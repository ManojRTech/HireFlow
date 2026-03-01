package com.manoj.hireflow.service;

import com.manoj.hireflow.dto.LoginRequestDto;
import com.manoj.hireflow.dto.LoginResponseDto;
import com.manoj.hireflow.dto.UserRegisterRequest;
import com.manoj.hireflow.entity.User;
import com.manoj.hireflow.exception.BadCredentialsException;
import com.manoj.hireflow.exception.UserNotFoundException;
import com.manoj.hireflow.repository.UserRepository;
import com.manoj.hireflow.security.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    private final UserRepository userRepository;



    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User register(UserRegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with this email");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        return userRepository.save(user);
    }


    public LoginResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        String token = JwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new LoginResponseDto(token, user.getRole().name());
    }

}
