package com.example.cursoapp.controller.content;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.dto.content.video.CreateVideoRequest;
import com.example.cursoapp.dto.content.video.VideoResponse;
import com.example.cursoapp.service.content.VideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    private ResponseEntity<GeneralResponse> buildResponse(Object data, String message, HttpStatus status) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
        return ResponseEntity.status(status).body(
                GeneralResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(status.value())
                        .time(Instant.now())
                        .data(data)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<GeneralResponse> createVideo(@Valid @RequestBody CreateVideoRequest request) {
        return buildResponse(videoService.createVideo(request), "Video created successfully.", HttpStatus.CREATED);
    }

    @GetMapping("/lection/{lectionId}")
    public ResponseEntity<GeneralResponse> getVideosByLection(@PathVariable Long lectionId) {
        return buildResponse(videoService.getVideosByLection(lectionId), "Videos retrieved successfully.", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getVideoById(@PathVariable Long id) {
        return buildResponse(videoService.getVideoById(id), "Video found.", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
        return buildResponse(null, "Video deleted successfully.", HttpStatus.OK);
    }
}