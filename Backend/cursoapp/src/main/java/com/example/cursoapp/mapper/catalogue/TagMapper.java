package com.example.cursoapp.mapper.catalogue;

import com.example.cursoapp.domain.entity.catalogue.Tag;
import com.example.cursoapp.dto.catalogue.tag.AdminTagResponse;
import com.example.cursoapp.dto.catalogue.tag.BasicTagResponse;
import com.example.cursoapp.dto.catalogue.tag.CreateTagRequest;
import com.example.cursoapp.dto.catalogue.tag.UpdateTagRequest;

import java.time.Instant;

public class TagMapper {
    public static BasicTagResponse toBasicDTO(Tag tag) {
        return BasicTagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    public static AdminTagResponse toAdminDTO(
            Tag tag
    ) {
        return AdminTagResponse.builder()
                .id(tag.getId())
                .currentName(tag.getName())
                .createdAt(tag.getCreatedAt())
                .build();
    }

    public static Tag toCreateEntity(
        CreateTagRequest createRequest
    ) {
        return Tag.builder()
                .name(createRequest.name())
                .build();
    }

    public static Tag toUpdateEntity(
            Tag entity,
            UpdateTagRequest updateRequest
    ) {
        if (updateRequest.name() != null) entity.setName(updateRequest.name());
        return entity;
    }
}