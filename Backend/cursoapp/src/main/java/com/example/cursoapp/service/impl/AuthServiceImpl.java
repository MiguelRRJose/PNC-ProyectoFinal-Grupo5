package com.example.cursoapp.service.impl;

import com.example.cursoapp.domain.entity.Usuario;
import com.example.cursoapp.dto.request.LoginRequest;
import com.example.cursoapp.dto.response.AuthResponse;
import com.example.cursoapp.exceptions.BusinessRuleException;
import com.example.cursoapp.repository.UsuarioRepository;
import com.example.cursoapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessRuleException("Invalid credentials."));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash()))
            throw new BusinessRuleException("Invalid credentials.");

        if (!usuario.getIsActive())
            throw new BusinessRuleException("User account is inactive.");

        return AuthResponse.builder()
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .role(usuario.getRole().getRoleName().name())
                .token("JWT_TOKEN_PLACEHOLDER")
                .build();
    }
}