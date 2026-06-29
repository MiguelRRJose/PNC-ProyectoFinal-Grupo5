package com.example.cursoapp.service.catalogue;

import com.example.cursoapp.dto.catalogue.favorite.BasicFavoriteResponse;
import com.example.cursoapp.dto.catalogue.favorite.CreateFavoriteRequest;

import java.util.List;
import java.util.UUID;

public interface FavoriteService {
    BasicFavoriteResponse createFavorite(CreateFavoriteRequest request, UUID userId);
    BasicFavoriteResponse deleteFavorite(UUID favoriteId);
    List<BasicFavoriteResponse> getAllFavoritesByUser(UUID userId);
}
