package com.example.cursoapp.mapper.catalogue;

import com.example.cursoapp.domain.entity.catalogue.Course;
import com.example.cursoapp.domain.entity.catalogue.Favorite;
import com.example.cursoapp.domain.entity.identity.Usuario;
import com.example.cursoapp.dto.catalogue.course.BasicCourseResponse;
import com.example.cursoapp.dto.catalogue.favorite.AdminFavoriteResponse;
import com.example.cursoapp.dto.catalogue.favorite.BasicFavoriteResponse;
import com.example.cursoapp.dto.catalogue.favorite.CreateFavoriteRequest;
import com.example.cursoapp.dto.identity.user.UserResponse;

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
            UserResponse user,
            BasicCourseResponse course
    ) {
        return AdminFavoriteResponse.builder()
                .id(entity.getId())
                .user(user)
                .course(course)
                .build();
    }

    public static Favorite toCreateEntity(
            CreateFavoriteRequest createRequest,
            Usuario user,
            Course course
    ) {
        return Favorite.builder()
                .user(user)
                .course(course)
                .build();
    }
}