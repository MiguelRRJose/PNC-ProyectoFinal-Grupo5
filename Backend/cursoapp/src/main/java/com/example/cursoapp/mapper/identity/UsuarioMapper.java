package com.example.cursoapp.mapper.identity;

import com.example.cursoapp.domain.entity.identity.Usuario;
import com.example.cursoapp.dto.identity.user.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UserResponse toDto(Usuario usuario) {
        return UserResponse.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .role(usuario.getRole().getName().name())
                .isActive(usuario.getIsActive())
                .createdAt(usuario.getCreatedAt())
                .build();
    }
}
