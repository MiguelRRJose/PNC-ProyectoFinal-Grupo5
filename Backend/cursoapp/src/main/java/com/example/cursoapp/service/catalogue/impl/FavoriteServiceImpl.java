package com.example.cursoapp.service.catalogue.impl;

import com.example.cursoapp.domain.entity.catalogue.Favorite;
import com.example.cursoapp.dto.catalogue.favorite.BasicFavoriteResponse;
import com.example.cursoapp.dto.catalogue.favorite.CreateFavoriteRequest;
import com.example.cursoapp.mapper.catalogue.FavoriteMapper;
import com.example.cursoapp.repository.catalogue.FavoriteRepository;
import com.example.cursoapp.service.catalogue.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteServiceImpl implements FavoriteService {
    private final CourseServiceImpl courseService;
    private final FavoriteRepository favoriteRepository;


    @Override
    public BasicFavoriteResponse createFavorite(CreateFavoriteRequest request, UUID userId) {
        Favorite favorite = favoriteRepository.save(
                FavoriteMapper.toCreateEntity(request, userId)
        );

        return FavoriteMapper.toBasicDTO(
                favorite, courseService.findBasicCourseById(favorite.getCourseId())
        );
    }

    @Override
    public BasicFavoriteResponse deleteFavorite(UUID favoriteId) {
        Favorite favorite = favoriteRepository.getReferenceById(favoriteId);
        favoriteRepository.delete(favorite);
        return FavoriteMapper.toBasicDTO(
                favorite, courseService.findBasicCourseById(favorite.getCourseId())
        );
    }

    @Override
    public List<BasicFavoriteResponse> getAllFavoritesByUser(UUID userId) {
        List<Favorite> favorites = favoriteRepository.findAll();
        return favorites.stream().map(favorite -> FavoriteMapper.toBasicDTO(
                favorite, courseService.findBasicCourseById(favorite.getCourseId())
        )).toList();
    }
}