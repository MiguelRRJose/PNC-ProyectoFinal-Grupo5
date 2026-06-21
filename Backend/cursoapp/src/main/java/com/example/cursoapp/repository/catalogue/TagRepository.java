package com.example.cursoapp.repository.catalogue;

import com.example.cursoapp.domain.entity.catalogue.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TagRepository extends JpaRepository<UUID, Tag> {
}