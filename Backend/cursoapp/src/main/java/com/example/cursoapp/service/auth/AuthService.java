package com.example.cursoapp.service.auth;

import com.example.cursoapp.dto.auth.AuthResponse;
import com.example.cursoapp.dto.auth.LoginRequest;
import com.example.cursoapp.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
