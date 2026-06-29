package com.example.cursoapp.domain.entity.progress;

import com.example.cursoapp.domain.entity.catalogue.Course;
import com.example.cursoapp.domain.entity.identity.Usuario;
import com.example.cursoapp.domain.enums.CertificateFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(
        name = "certified",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"})
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Certified {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Usuario user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @CreationTimestamp
    @Column(name = "certification_date", updatable = false)
    private Instant certificationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "format", nullable = false)
    private CertificateFormat format;

    @Column(name = "url")
    private String url;
}
