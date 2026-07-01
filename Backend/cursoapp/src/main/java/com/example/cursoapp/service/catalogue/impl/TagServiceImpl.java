package com.example.cursoapp.service.catalogue.impl;

import com.example.cursoapp.domain.entity.catalogue.Tag;
import com.example.cursoapp.dto.catalogue.tag.AdminTagResponse;
import com.example.cursoapp.dto.catalogue.tag.BasicTagResponse;
import com.example.cursoapp.dto.catalogue.tag.CreateTagRequest;
import com.example.cursoapp.dto.catalogue.tag.UpdateTagRequest;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.catalogue.TagMapper;
import com.example.cursoapp.repository.catalogue.TagRepository;
import com.example.cursoapp.service.catalogue.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    private Tag getByIdOrThrow(Long tagId) {
        return tagRepository.findById(tagId).orElseThrow(
                () -> new ResourceNotFoundException("Tag not found with id: " + tagId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BasicTagResponse findById(Long id) {
        Tag tag = getByIdOrThrow(id);

        return TagMapper.toBasicDTO(tag);
    }

    // Cuando hice esto, no había pensado en cómo el servicio diferenciaría entre sí el usuario que hace el
    // request es un Administrador o no... Hmmm...
    @Override
    @Transactional(readOnly = true)
    public AdminTagResponse findByIdAdmin(Long id) {
        Tag tag = getByIdOrThrow(id);

        return TagMapper.toAdminDTO(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BasicTagResponse> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(TagMapper::toBasicDTO)
                .toList();
    }

    @Override
    public AdminTagResponse createTag(CreateTagRequest request) {
        Tag tag = tagRepository.save(TagMapper.toCreateEntity(request));
        return TagMapper.toAdminDTO(tag);
    }

    @Override
    public AdminTagResponse updateTag(Long id, UpdateTagRequest request) {
        Tag tag = getByIdOrThrow(id);
        Tag updatedTag = tagRepository.save(TagMapper.toUpdateEntity(tag, request));

        return TagMapper.toAdminDTO(updatedTag);
    }

    @Override
    public AdminTagResponse deleteTag(Long id) {
        Tag tag = getByIdOrThrow(id);
        tagRepository.delete(tag);
        return TagMapper.toAdminDTO(tag);
    }
}