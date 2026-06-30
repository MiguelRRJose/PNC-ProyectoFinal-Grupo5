package com.example.cursoapp.service.catalogue.impl;

import com.example.cursoapp.domain.entity.catalogue.Favorite;
import com.example.cursoapp.domain.entity.identity.Usuario;
import com.example.cursoapp.dto.catalogue.favorite.BasicFavoriteResponse;
import com.example.cursoapp.dto.catalogue.favorite.CreateFavoriteRequest;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.catalogue.FavoriteMapper;
import com.example.cursoapp.repository.catalogue.CourseRepository;
import com.example.cursoapp.repository.catalogue.FavoriteRepository;
import com.example.cursoapp.repository.identity.UsuarioRepository;
import com.example.cursoapp.service.catalogue.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteServiceImpl implements FavoriteService {
    private final CourseServiceImpl courseService;
    private final CourseRepository courseRepository;
    private final FavoriteRepository favoriteRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public BasicFavoriteResponse createFavorite(CreateFavoriteRequest request, Long userId) {
        Usuario user = usuarioRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Favorite favorite = favoriteRepository.save(
                FavoriteMapper.toCreateEntity(request, user, courseRepository.getReferenceById(request.courseId()))
        );

        return FavoriteMapper.toBasicDTO(
                favorite, courseService.findBasicCourseById(favorite.getCourse().getId())
        );
    }

    @Override
    public BasicFavoriteResponse deleteFavorite(Long favoriteId) {
        Favorite favorite = favoriteRepository.getReferenceById(favoriteId);
        favoriteRepository.delete(favorite);
        return FavoriteMapper.toBasicDTO(
                favorite, courseService.findBasicCourseById(favorite.getCourse().getId())
        );
    }

    @Override
    public List<BasicFavoriteResponse> getAllFavoritesByUser(Long userId) {
        List<Favorite> favorites = favoriteRepository.findAll();
        return favorites.stream().map(favorite -> FavoriteMapper.toBasicDTO(
                favorite, courseService.findBasicCourseById(favorite.getCourse().getId())
        )).toList();
    }
}