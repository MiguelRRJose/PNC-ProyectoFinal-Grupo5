package com.example.cursoapp.domain.entity.progress;

import com.example.cursoapp.domain.entity.content.Lection;
import com.example.cursoapp.domain.entity.identity.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "completions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "lection_id"})
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Completion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Usuario user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lection_id", nullable = false)
    private Lection lection;

    @Builder.Default
    @Column(name = "is_completed")
    private Boolean isCompleted = false;
}
