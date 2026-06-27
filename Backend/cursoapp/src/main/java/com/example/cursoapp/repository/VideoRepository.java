package com.example.cursoapp.repository;

import com.example.cursoapp.domain.entity.content.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByLectionId(Long lectionId);
}