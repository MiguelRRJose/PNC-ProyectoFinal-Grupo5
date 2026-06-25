package com.example.cursoapp.service.impl;

import com.example.cursoapp.domain.entity.Role;
import com.example.cursoapp.domain.entity.RoleType;
import com.example.cursoapp.domain.entity.Usuario;
import com.example.cursoapp.dto.request.RegisterRequest;
import com.example.cursoapp.dto.response.UsuarioResponse;
import com.example.cursoapp.exceptions.BusinessRuleException;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.UsuarioMapper;
import com.example.cursoapp.repository.RoleRepository;
import com.example.cursoapp.repository.UsuarioRepository;
import com.example.cursoapp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioResponse registerUser(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail()))
            throw new BusinessRuleException("Email already in use: " + request.getEmail());
        if (usuarioRepository.existsByUsername(request.getUsername()))
            throw new BusinessRuleException("Username already in use: " + request.getUsername());

        Role role = roleRepository.findByRoleName(RoleType.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found."));

        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .isActive(true)
                .creationDate(LocalDateTime.now())
                .build();

        return usuarioMapper.toDto(usuarioRepository.save(usuario));
    }

    @Override
    public List<UsuarioResponse> getAllUsers() {
        return usuarioRepository.findAll()
                .stream().map(usuarioMapper::toDto).toList();
    }

    @Override
    public UsuarioResponse getUserById(Long id) {
        return usuarioMapper.toDto(usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id)));
    }

    @Override
    @Transactional
    public UsuarioResponse updateUserStatus(Long id, Boolean isActive) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        usuario.setIsActive(isActive);
        return usuarioMapper.toDto(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        usuarioRepository.deleteById(id);
    }
}