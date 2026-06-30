package com.example.cursoapp.repository.catalogue;

import com.example.cursoapp.domain.entity.catalogue.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}