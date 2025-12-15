package com.techup.spring.spring_be.controller;

import com.techup.spring.spring_be.config.JwtTokenProvider;
import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.auth.AuthResponse;
import com.techup.spring.spring_be.dto.auth.LoginRequest;
import com.techup.spring.spring_be.dto.auth.RegisterRequest;
import com.techup.spring.spring_be.dto.common.ApiResponse;
import com.techup.spring.spring_be.dto.user.UserResponse;
import com.techup.spring.spring_be.repository.UserRepository;
import com.techup.spring.spring_be.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse res = authService.register(request);
        return ApiResponse.ok("회원가입 성공", res);
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse res = authService.login(request);
        return ApiResponse.ok("로그인 성공", res);
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getMyInfo(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer", "").trim();
        String email = jwtTokenProvider.getEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse res = new UserResponse(
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getProfileImage(),
                user.getCommunity().getName()
        );

        return ApiResponse.ok("내 정보 조회 성공", res);
    }
}