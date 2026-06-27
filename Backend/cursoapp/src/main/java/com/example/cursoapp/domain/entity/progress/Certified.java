package com.example.cursoapp.domain.entity.progress;

import com.example.cursoapp.domain.entity.catalogue.Course;
import com.example.cursoapp.domain.enums.CertificateFormat;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

//@Entity
//@Table(
//        name = "certified",
//        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"})
//)
//public class Certified {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private UUID id;
//
//
//    private User user;
//
//    private Course course;
//
//    private Instant certificationDateTime;
//
//    private CertificateFormat format;
//
//    private String URL;
//
//
//}