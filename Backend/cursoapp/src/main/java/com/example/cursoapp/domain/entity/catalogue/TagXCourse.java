package com.example.cursoapp.domain.entity.catalogue;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tag_x_course")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagXCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}