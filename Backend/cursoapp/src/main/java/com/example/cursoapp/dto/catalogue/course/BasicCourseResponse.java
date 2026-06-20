package com.example.cursoapp.dto.catalogue.course;

import com.example.cursoapp.dto.catalogue.tag.BasicTagResponse;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


public record BasicCourseResponse(
        UUID id,
        String name,
        Double price,
        List<BasicTagResponse> tags,
        // BasicUserResponse instructor, // TODO: A basic response with minimum info needed
        Instant createdAt,

        // Estadísticas públicas, cualquiera puede verlas
        Double averageRating,
        Long reviewCount
) {
}