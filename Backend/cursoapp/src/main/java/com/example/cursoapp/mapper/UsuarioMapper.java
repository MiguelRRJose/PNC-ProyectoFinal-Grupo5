package com.example.cursoapp.mapper;

import com.example.cursoapp.domain.entity.Usuario;
import com.example.cursoapp.dto.response.UsuarioResponse;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponse toDto(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .roleName(usuario.getRole().getRoleName().name())
                .isActive(usuario.getIsActive())
                .creationDate(usuario.getCreationDate())
                .build();
    }
}