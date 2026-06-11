package com.example.cursoapp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lections")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;
}