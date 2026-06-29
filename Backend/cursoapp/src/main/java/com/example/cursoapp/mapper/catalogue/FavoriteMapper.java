package com.example.cursoapp.mapper.catalogue;

import com.example.cursoapp.domain.entity.catalogue.Favorite;
import com.example.cursoapp.dto.catalogue.course.BasicCourseResponse;
import com.example.cursoapp.dto.catalogue.favorite.AdminFavoriteResponse;
import com.example.cursoapp.dto.catalogue.favorite.BasicFavoriteResponse;
import com.example.cursoapp.dto.catalogue.favorite.CreateFavoriteRequest;

import java.util.UUID;

public class FavoriteMapper {
    public static BasicFavoriteResponse toBasicDTO(
            Favorite entity,
            BasicCourseResponse course
    ) {
        return BasicFavoriteResponse.builder()
                .id(entity.getId())
                .course(course)
                .build();
    }

    public static AdminFavoriteResponse toAdminDTO(
            Favorite entity,
            // AdminUserResponse user, //TODO: When the User responses are done, fix this
            BasicCourseResponse course
    ) {
        return AdminFavoriteResponse.builder()
                .id(entity.getId())
                // .user(user)
                .course(course)
                .build();
    }

    public static Favorite toCreateEntity(
            CreateFavoriteRequest createRequest,
            UUID userId
    ) {
        return Favorite.builder()
                .userId(userId)
                .courseId(createRequest.courseId())
                .build();
    }
}