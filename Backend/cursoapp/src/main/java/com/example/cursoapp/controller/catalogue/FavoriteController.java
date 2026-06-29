package com.example.cursoapp.controller.catalogue;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.dto.catalogue.favorite.CreateFavoriteRequest;
import com.example.cursoapp.service.catalogue.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    private ResponseEntity<GeneralResponse> buildResponse(Object data, String message, HttpStatus status) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
        return ResponseEntity
                .status(status)
                .body(GeneralResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(status.value())
                        .time(Instant.now())
                        .data(data)
                        .build()
                );
    }

    @GetMapping("/by-user")
    public ResponseEntity<GeneralResponse> getFavoritesByUser() {
        UUID userId = null;
        return buildResponse(
                //TODO: Then again, JWT to get the userId
                favoriteService.getAllFavoritesByUser(userId),
                "Favorites successfully found.",
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<GeneralResponse> addFavorite(@RequestBody CreateFavoriteRequest request) {
        UUID userId = null; //TODO: Another temporary null that needs fixing
        return buildResponse(
                favoriteService.createFavorite(request, userId),
                "Favorite successfully added.",
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteFavorite(@PathVariable UUID id) {
        return buildResponse(
                favoriteService.deleteFavorite(id),
                "Favorite successfully deleted.",
                HttpStatus.OK
        );
    }
}