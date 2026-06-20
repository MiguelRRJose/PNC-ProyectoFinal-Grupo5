package com.example.cursoapp.domain.entity.content;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "questions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Usuario user;

    @ManyToOne
    @JoinColumn(name = "lection_id", nullable = false)
    private Lection lection;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;
}