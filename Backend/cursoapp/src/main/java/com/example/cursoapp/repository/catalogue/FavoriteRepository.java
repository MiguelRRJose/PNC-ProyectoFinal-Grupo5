package com.example.cursoapp.repository.catalogue;

import com.example.cursoapp.domain.entity.catalogue.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
    List<Favorite> findFavoriteByCourseIdEquals(UUID courseId);
}