package com.example.cursoapp.repository;

import com.example.cursoapp.domain.entity.content.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByLectionId(Long lectionId);
}