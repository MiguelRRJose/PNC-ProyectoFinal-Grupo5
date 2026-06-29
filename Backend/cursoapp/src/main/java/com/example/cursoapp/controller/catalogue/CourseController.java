package com.example.cursoapp.controller.catalogue;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.dto.catalogue.course.CreateCourseRequest;
import com.example.cursoapp.dto.catalogue.course.UpdateCourseRequest;
import com.example.cursoapp.service.catalogue.impl.CourseServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.UUID;

//TODO: Modify the endpoints so that they return an appropriate response based on the User's role

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseServiceImpl courseService;

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
                courseService.getAllCourses(),
                "Courses successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getById(@PathVariable UUID id) {
        return buildResponse(
                courseService.findBasicCourseById(id),
                "Course successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<GeneralResponse> getByTagId(@PathVariable UUID tagId) {
        return buildResponse(
                courseService.getAllCoursesByTag(tagId),
                "Courses successfully found.",
                HttpStatus.OK
        );
    }

    // Only Instructors and Admins should be able to do the following methods

    @PostMapping
    public ResponseEntity<GeneralResponse> createCourse(@RequestBody @Valid CreateCourseRequest request) {
        return buildResponse(
                courseService.createCourse(request),
                "Course successfully created.",
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> updateCourse(@PathVariable UUID id, @RequestBody @Valid UpdateCourseRequest request) {
        return buildResponse(
                courseService.updateCourse(id, request),
                "Course successfully updated.",
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<GeneralResponse> publishCourse(@PathVariable UUID id) {
        return buildResponse(
                courseService.publishCourse(id),
                "Course successfully published.",
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}/unpublish")
    public ResponseEntity<GeneralResponse> unpublishCourse(@PathVariable UUID id) {
        return buildResponse(
                courseService.unpublishCourse(id),
                "Course successfully published.",
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteCourse(@PathVariable UUID id) {
        return buildResponse(
                courseService.deleteCourse(id),
                "Course successfully removed. The course is still stored in the database for audit purposes.",
                HttpStatus.OK
        );
    }

    // This thing only Admins should be able to do it

    @PutMapping("/{id}/restore")
    public ResponseEntity<GeneralResponse> restoreCourse(@PathVariable UUID id) {
        return buildResponse(
                courseService.restoreCourse(id),
                "Course successfully published.",
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}/")
}