package com.example.cursoapp.service.identity.impl;

import com.example.cursoapp.domain.entity.identity.Usuario;
import com.example.cursoapp.dto.identity.user.UpdateUserRequest;
import com.example.cursoapp.dto.identity.user.UserResponse;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.identity.UsuarioMapper;
import com.example.cursoapp.repository.identity.UsuarioRepository;
import com.example.cursoapp.service.identity.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    private Usuario getByIdOrThrow(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return usuarioMapper.toDto(getByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return usuarioRepository.findAll()
                .stream().map(usuarioMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByRole(Long roleId) {
        return usuarioRepository.findByRoleId(roleId)
                .stream().map(usuarioMapper::toDto).toList();
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        Usuario usuario = getByIdOrThrow(id);
        if (request.getUsername() != null) usuario.setUsername(request.getUsername());
        if (request.getEmail() != null) usuario.setEmail(request.getEmail());
        return usuarioMapper.toDto(usuarioRepository.save(usuario));
    }

    @Override
    public UserResponse deactivateUser(Long id) {
        Usuario usuario = getByIdOrThrow(id);
        usuario.setIsActive(false);
        return usuarioMapper.toDto(usuarioRepository.save(usuario));
    }

    @Override
    public UserResponse activateUser(Long id) {
        Usuario usuario = getByIdOrThrow(id);
        usuario.setIsActive(true);
        return usuarioMapper.toDto(usuarioRepository.save(usuario));
    }
}
