package com.example.cursoapp.dto.catalogue.course;

import com.example.cursoapp.dto.catalogue.tag.BasicTagResponse;
import com.example.cursoapp.dto.identity.user.UserResponse;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record AdminCourseResponse (
        // Datos del curso para el instructor
        Long id,
        String name,
        Double price,
        List<BasicTagResponse> tags,
        Instant createdAt,

        // Datos administrativos, supongo
        Boolean isPublished,
        Boolean isDeleted,

        // Estadísticas del curso
        Double averageRating,
        Long reviewCount,
        Long enrollmentCount,
        Long certifiedCount,
        Double totalRevenue,

        // Auditoría
        UserResponse instructor
) {
}
