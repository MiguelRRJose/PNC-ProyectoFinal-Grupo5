package com.example.cursoapp.domain.entity.content;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "videos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lection_id", nullable = false)
    private Lection lection;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;
}