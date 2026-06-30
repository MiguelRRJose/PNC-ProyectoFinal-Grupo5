package com.example.cursoapp.mapper.catalogue;

import com.example.cursoapp.domain.entity.catalogue.Course;
import com.example.cursoapp.domain.entity.catalogue.Tag;
import com.example.cursoapp.dto.catalogue.course.*;
import com.example.cursoapp.dto.catalogue.tag.BasicTagResponse;
import com.example.cursoapp.dto.identity.user.UserResponse;
import com.example.cursoapp.mapper.identity.UsuarioMapper;

import java.time.Instant;
import java.util.List;

public class CourseMapper {
    private static UsuarioMapper userMapper = new UsuarioMapper();

    public static BasicCourseResponse toBasicDTO (
            Course course,
            List<BasicTagResponse> tags,
            Double averageRating,
            Long reviewCount
    ) {
        return BasicCourseResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .price(course.getPrice())
                .tags(tags)
                .instructor(userMapper.toDto(course.getInstructor()))
                .createdAt(course.getCreatedAt())
                .averageRating(averageRating)
                .reviewCount(reviewCount)
                .build();
    }

    public static InstructorCourseResponse toInstructorDTO (
            Course course,
            List<BasicTagResponse> tags,
            Double averageRating,
            Long reviewCount,
            Long enrollmentCount,
            Long certifiedCount,
            Double totalRevenue

    ) {
        return InstructorCourseResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .price(course.getPrice())
                .tags(tags)
                .createdAt(course.getCreatedAt())
                .isPublished(course.getIsPublished())
                .isDeleted(course.getIsDeleted())
                .averageRating(averageRating)
                .reviewCount(reviewCount)
                .enrollmentCount(enrollmentCount)
                .certifiedCount(certifiedCount)
                .totalRevenue(totalRevenue)
                .build();
    }

    public static AdminCourseResponse toAdminDTO (
            Course course,
            List<BasicTagResponse> tags,
            Double averageRating,
            Long reviewCount,
            Long enrollmentCount,
            Long certifiedCount,
            Double totalRevenue
    ) {
        return AdminCourseResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .price(course.getPrice())
                .tags(tags)
                .createdAt(course.getCreatedAt())
                .isPublished(course.getIsPublished())
                .isDeleted(course.getIsDeleted())
                .averageRating(averageRating)
                .reviewCount(reviewCount)
                .enrollmentCount(enrollmentCount)
                .certifiedCount(certifiedCount)
                .totalRevenue(totalRevenue)
                .instructor(userMapper.toDto(course.getInstructor()))
                .build();
    }

    public static Course toCreateEntity (
            CreateCourseRequest createRequest,
            List<Tag> tags
    ) {
        return Course.builder()
                .name(createRequest.name())
                .price(createRequest.price())
                .tags(tags)
                .build();
    }

    public static Course toUpdateEntity (
            Course entity,
            UpdateCourseRequest updateRequest,
            List<Tag> tags
    ) {
        if (updateRequest.name() != null) entity.setName(updateRequest.name());
        if (updateRequest.price() != null) entity.setPrice(updateRequest.price());
        if (!tags.isEmpty()) entity.setTags(tags);

        return entity;
    }
}