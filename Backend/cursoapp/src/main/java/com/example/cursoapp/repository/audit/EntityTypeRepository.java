package com.example.cursoapp.repository.audit;

import com.example.cursoapp.domain.entity.audit.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntityTypeRepository extends JpaRepository<EntityType, Long> {
    Optional<EntityType> findByEntityName(String entityName);
}
