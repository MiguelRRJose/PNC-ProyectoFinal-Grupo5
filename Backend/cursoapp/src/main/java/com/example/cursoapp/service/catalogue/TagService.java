package com.example.cursoapp.service.catalogue;

import com.example.cursoapp.dto.catalogue.tag.AdminTagResponse;
import com.example.cursoapp.dto.catalogue.tag.BasicTagResponse;
import com.example.cursoapp.dto.catalogue.tag.CreateTagRequest;
import com.example.cursoapp.dto.catalogue.tag.UpdateTagRequest;

import java.util.List;
import java.util.UUID;

public interface TagService {
    BasicTagResponse findById(UUID id);

    AdminTagResponse findByIdAdmin(UUID id);

    List<BasicTagResponse> getAllTags();

    AdminTagResponse createTag(CreateTagRequest request);

    AdminTagResponse updateTag(UUID id, UpdateTagRequest request);

    AdminTagResponse deleteTag(UUID id);
}