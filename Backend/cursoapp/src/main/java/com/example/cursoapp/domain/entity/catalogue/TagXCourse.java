package com.example.cursoapp.domain.entity.catalogue;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tag_x_course")
public class TagXCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @MapsId("tag_id")
    @JoinColumn(name = "id")
    private Tag tag;

    @ManyToOne
    @MapsId("course_id")
    @JoinColumn(name = "id")
    private Course course;
}