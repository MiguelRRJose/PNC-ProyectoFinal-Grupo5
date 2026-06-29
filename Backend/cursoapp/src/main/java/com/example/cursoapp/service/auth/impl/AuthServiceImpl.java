package com.example.cursoapp.service.auth.impl;

import com.example.cursoapp.config.JwtUtil;
import com.example.cursoapp.domain.entity.identity.Role;
import com.example.cursoapp.domain.entity.identity.Usuario;
import com.example.cursoapp.domain.enums.RoleName;
import com.example.cursoapp.dto.auth.AuthResponse;
import com.example.cursoapp.dto.auth.LoginRequest;
import com.example.cursoapp.dto.auth.RegisterRequest;
import com.example.cursoapp.exceptions.BusinessRuleException;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.repository.identity.RoleRepository;
import com.example.cursoapp.repository.identity.UsuarioRepository;
import com.example.cursoapp.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new BusinessRuleException("Username already in use: " + request.getUsername());
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException("Email already registered: " + request.getEmail());
        }

        Role role = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Default role USER not found."));

        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        usuarioRepository.save(usuario);

        // Autenticamos para generar el token igual que en login
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        return AuthResponse.builder()
                .token(jwtUtil.generateToken(authentication))
                .username(usuario.getUsername())
                .role(role.getName().name())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        Usuario usuario = usuarioRepository
                .findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (!usuario.getIsActive()) {
            throw new BusinessRuleException("Account is deactivated.");
        }

        return AuthResponse.builder()
                .token(jwtUtil.generateToken(authentication))
                .username(usuario.getUsername())
                .role(usuario.getRole().getName().name())
                .build();
    }
}
