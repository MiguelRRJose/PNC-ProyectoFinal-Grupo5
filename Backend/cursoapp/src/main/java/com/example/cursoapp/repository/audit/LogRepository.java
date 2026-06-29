package com.example.cursoapp.repository.audit;

import com.example.cursoapp.domain.entity.audit.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findByUserId(Long userId);
    List<Log> findByEntityTypeId(Long entityTypeId);
    List<Log> findByActionId(Long actionId);
    List<Log> findByEntityId(Long entityId);
}
