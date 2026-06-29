package com.example.cursoapp.repository.audit;

import com.example.cursoapp.domain.entity.audit.Action;
import com.example.cursoapp.domain.enums.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
    Optional<Action> findByName(ActionType name);
}
