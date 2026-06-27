package com.example.cursoapp.dto.catalogue.course;

import com.example.cursoapp.dto.catalogue.tag.BasicTagResponse;
import com.example.cursoapp.dto.content.module.ModuleResponse;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record InstructorCourseResponse(
        // Datos del curso para el instructor
        UUID id,
        String name,
        Double price,
        List<BasicTagResponse> tags,
        Instant createdAt,

        // Datos administrativos, supongo
        Boolean isPublished,
        Boolean isDeleted,
        // Para que se entere el instructor si un administrador ha eliminado su curso,
        // aunque de momento no pueda hacer nada al respecto.

        // Estadísticas públicas
        Double averageRating,
        Integer reviewCount,

        // Estadísticas propias del instructor
        Integer enrollmentCount,
        Integer certifiedCount,
        Double totalRevenue
) {
}
