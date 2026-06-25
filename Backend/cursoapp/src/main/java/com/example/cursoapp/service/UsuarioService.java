package com.example.cursoapp.service;

import com.example.cursoapp.dto.request.RegisterRequest;
import com.example.cursoapp.dto.response.UsuarioResponse;

import java.util.List;

public interface UsuarioService {
    UsuarioResponse registerUser(RegisterRequest request);
    List<UsuarioResponse> getAllUsers();
    UsuarioResponse getUserById(Long id);
    UsuarioResponse updateUserStatus(Long id, Boolean isActive);
    void deleteUser(Long id);
}