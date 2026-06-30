package com.example.cursoapp.repository.catalogue;

import com.example.cursoapp.domain.entity.catalogue.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findFavoriteByCourseIdEquals(Long courseId);
}