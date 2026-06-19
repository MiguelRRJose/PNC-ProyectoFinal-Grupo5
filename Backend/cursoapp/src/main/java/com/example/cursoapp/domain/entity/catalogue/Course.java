package com.example.cursoapp.domain.entity.catalogue;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Table(name = "course")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "instructor_id")
    private UUID instructorId;

    @Column(name = "is_published")
    private Boolean isPublished;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}