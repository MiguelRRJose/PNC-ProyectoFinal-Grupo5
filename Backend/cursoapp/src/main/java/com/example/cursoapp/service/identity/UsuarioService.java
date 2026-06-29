package com.example.cursoapp.service.identity;

import com.example.cursoapp.dto.identity.user.UpdateUserRequest;
import com.example.cursoapp.dto.identity.user.UserResponse;

import java.util.List;

public interface UsuarioService {
    UserResponse getUserById(Long id);
    List<UserResponse> getAllUsers();
    List<UserResponse> getUsersByRole(Long roleId);
    UserResponse updateUser(Long id, UpdateUserRequest request);
    UserResponse deactivateUser(Long id);
    UserResponse activateUser(Long id);
}
