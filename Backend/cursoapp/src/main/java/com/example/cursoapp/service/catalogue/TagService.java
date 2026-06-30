package com.example.cursoapp.service.catalogue;

import com.example.cursoapp.dto.catalogue.tag.AdminTagResponse;
import com.example.cursoapp.dto.catalogue.tag.BasicTagResponse;
import com.example.cursoapp.dto.catalogue.tag.CreateTagRequest;
import com.example.cursoapp.dto.catalogue.tag.UpdateTagRequest;

import java.util.List;

public interface TagService {
    BasicTagResponse findById(Long id);

    AdminTagResponse findByIdAdmin(Long id);

    List<BasicTagResponse> getAllTags();

    AdminTagResponse createTag(CreateTagRequest request);

    AdminTagResponse updateTag(Long id, UpdateTagRequest request);

    AdminTagResponse deleteTag(Long id);
}