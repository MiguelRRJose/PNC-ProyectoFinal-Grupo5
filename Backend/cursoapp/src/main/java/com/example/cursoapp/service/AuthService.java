package com.example.cursoapp.service;

import com.example.cursoapp.dto.request.LoginRequest;
import com.example.cursoapp.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
}