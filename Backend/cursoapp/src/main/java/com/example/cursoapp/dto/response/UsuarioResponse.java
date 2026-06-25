package com.example.cursoapp.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String username;
    private String email;
    private String roleName;
    private Boolean isActive;
    private LocalDateTime creationDate;
}