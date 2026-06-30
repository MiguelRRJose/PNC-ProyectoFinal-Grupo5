package com.example.cursoapp.dto.catalogue.course;

import com.example.cursoapp.domain.entity.identity.Usuario;
import com.example.cursoapp.dto.catalogue.tag.BasicTagResponse;
import com.example.cursoapp.dto.identity.user.UserResponse;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record BasicCourseResponse(
        Long id,
        String name,
        Double price,
        List<BasicTagResponse> tags,
        UserResponse instructor,
        Instant createdAt,

        // Estadísticas públicas, cualquiera puede verlas
        Double averageRating,
        Long reviewCount
) {
}