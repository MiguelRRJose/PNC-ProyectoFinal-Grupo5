package com.example.cursoapp.service.catalogue;

import com.example.cursoapp.dto.catalogue.favorite.BasicFavoriteResponse;
import com.example.cursoapp.dto.catalogue.favorite.CreateFavoriteRequest;

import java.util.List;

public interface FavoriteService {
    BasicFavoriteResponse createFavorite(CreateFavoriteRequest request, Long userId);
    BasicFavoriteResponse deleteFavorite(Long favoriteId);
    List<BasicFavoriteResponse> getAllFavoritesByUser(Long userId);
}
