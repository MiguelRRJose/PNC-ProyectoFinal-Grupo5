package com.example.cursoapp.domain.entity.content;

import com.example.cursoapp.domain.entity.catalogue.Course;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "modules")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "index", nullable = false)
    private Integer index;
}