package com.example.cursoapp.dto.catalogue.tag;

import java.util.UUID;

public record BasicTagResponse(
    UUID id,
    String name
) {
}

// There is kind of a problem when thinking about deleting a tag
// considering a Course must have at least one tag.
// TODO: What happens when a tag is deleted, and a course has just that tag assigned to it?