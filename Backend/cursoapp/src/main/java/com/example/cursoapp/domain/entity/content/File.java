package com.example.cursoapp.domain.entity.content;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "files")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lection_id", nullable = false)
    private Lection lection;

    @Column(name = "path_to_file", nullable = false)
    private String pathToFile;
}