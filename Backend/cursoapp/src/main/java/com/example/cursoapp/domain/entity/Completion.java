package com.example.cursoapp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "completions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Completion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Usuario user;

    @ManyToOne
    @JoinColumn(name = "lection_id", nullable = false)
    private Lection lection;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;
}