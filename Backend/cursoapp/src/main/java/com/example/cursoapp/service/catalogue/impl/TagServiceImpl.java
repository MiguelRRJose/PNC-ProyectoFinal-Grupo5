package com.example.cursoapp.service.catalogue.impl;

import com.example.cursoapp.domain.entity.catalogue.Tag;
import com.example.cursoapp.dto.catalogue.tag.AdminTagResponse;
import com.example.cursoapp.dto.catalogue.tag.BasicTagResponse;
import com.example.cursoapp.dto.catalogue.tag.CreateTagRequest;
import com.example.cursoapp.dto.catalogue.tag.UpdateTagRequest;
import com.example.cursoapp.mapper.catalogue.TagMapper;
import com.example.cursoapp.repository.catalogue.TagRepository;
import com.example.cursoapp.service.catalogue.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    //TODO: When exceptions are done, use one of them here instead of the wrongly used IllegalStateException
    @Override
    @Transactional(readOnly = true)
    public BasicTagResponse findById(UUID id) {
        Tag tag = tagRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("The ID provided does not belong to any existing Tag.") // () -> new ResourceNotFoundException("Tag not found with id: " + id)
        );

        return TagMapper.toBasicDTO(tag);
    }

    // Cuando hice esto, no había pensado en cómo el servicio diferenciaría entre sí el usuario que hace el
    // request es un Administrador o no... Hmmm...
    @Override
    @Transactional(readOnly = true)
    public AdminTagResponse findByIdAdmin(UUID id) {
        Tag tag = tagRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("The ID provided does not belong to any existing Tag.") // () -> new ResourceNotFoundException("Tag not found with id: " + id)
        );

        //TODO: Somehow, I will be able to do this properly later

        Instant lastModifiedAt = null;
        String lastModifiedBy = null;

        return TagMapper.toAdminDTO(tag, lastModifiedAt, lastModifiedBy);
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
        Tag tag = TagMapper.toCreateEntity(request);

        //TODO: Here, there needs to be an audit of the creation of the Tag.

        Instant lastModifiedAt = null;
        String lastModifiedBy = null;

        return TagMapper.toAdminDTO(
                tagRepository.save(TagMapper.toCreateEntity(request)),
                lastModifiedAt, lastModifiedBy
        );
    }

    @Override
    public AdminTagResponse updateTag(UUID id, UpdateTagRequest request) {
        Tag tag = tagRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("The ID provided does not belong to any existing Tag.") // () -> new ResourceNotFoundException("Tag not found with id: " + id)
        );

        Tag updatedTag = tagRepository.save(TagMapper.toUpdateEntity(tag, request));

        //TODO: Here, there needs to be an audit of the updating of the Tag.

        Instant lastModifiedAt = null;
        String lastModifiedBy = null;

        return TagMapper.toAdminDTO(updatedTag, lastModifiedAt, lastModifiedBy);
    }

    @Override
    public AdminTagResponse deleteTag(UUID id) {
        Tag tag = tagRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("The ID provided does not belong to any existing Tag.") // () -> new ResourceNotFoundException("Tag not found with id: " + id)
        );

        //TODO: Here, there needs to be an audit of the deletion of the Tag.

        Instant lastModifiedAt = null;
        String lastModifiedBy = null;

        tagRepository.delete(tag);

        return TagMapper.toAdminDTO(tag, lastModifiedAt, lastModifiedBy);
    }
}