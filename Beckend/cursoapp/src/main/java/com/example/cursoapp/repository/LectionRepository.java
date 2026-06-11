package com.example.cursoapp.repository;

import com.example.cursoapp.domain.entity.Lection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectionRepository extends JpaRepository<Lection, Long> {
    List<Lection> findByModuleId(Long moduleId);
}