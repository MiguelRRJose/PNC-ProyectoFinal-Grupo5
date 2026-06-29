package com.example.cursoapp.controller.catalogue;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.dto.catalogue.tag.CreateTagRequest;
import com.example.cursoapp.dto.catalogue.tag.UpdateTagRequest;
import com.example.cursoapp.service.catalogue.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.UUID;

// TODO: Apply logic for role based responses later

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

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

    @GetMapping
    public ResponseEntity<GeneralResponse> getAll() {
        return buildResponse(
                tagService.getAllTags(),
                "Tags successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getById(@PathVariable UUID id) {
        return buildResponse(
                tagService.findById(id),
                "Tag successfully found.",
                HttpStatus.OK
        );
    }

    // Only Admins should be able to do the following actions

    @PostMapping
    public ResponseEntity<GeneralResponse> createTag(@RequestBody CreateTagRequest request) {
        return buildResponse(
                tagService.createTag(request),
                "Tag successfully created.",
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> updateTag(@PathVariable UUID id, @RequestBody UpdateTagRequest request) {
        return buildResponse(
                tagService.updateTag(id, request),
                "Tag successfully updated.",
                HttpStatus.OK
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<GeneralResponse> deleteTag(@PathVariable UUID id) {
        return buildResponse(
                tagService.deleteTag(id),
                "Tag successfully deleted.",
                HttpStatus.OK
        );
    }
}