package com.example.cursoapp.controller.content;

import com.example.cursoapp.dto.content.video.CreateVideoRequest;
import com.example.cursoapp.dto.response.GeneralResponse;
import com.example.cursoapp.dto.content.video.VideoResponse;
import com.example.cursoapp.service.content.impl.VideoServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoServiceImpl videoService;

    private ResponseEntity<GeneralResponse> buildResponse(Object data, String message, HttpStatus status, HttpServletRequest request) {
        return ResponseEntity.status(status).body(
                GeneralResponse.builder()
                        .data(data)
                        .message(message)
                        .status(status.value())
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<GeneralResponse> createVideo(
            @Valid @RequestBody CreateVideoRequest request,
            HttpServletRequest httpRequest) {
        VideoResponse response = videoService.createVideo(request);
        return buildResponse(response, "Video created successfully.", HttpStatus.CREATED, httpRequest);
    }

    @GetMapping("/lection/{lectionId}")
    public ResponseEntity<GeneralResponse> getVideosByLection(
            @PathVariable Long lectionId,
            HttpServletRequest httpRequest) {
        List<VideoResponse> response = videoService.getVideosByLection(lectionId);
        return buildResponse(response, "Videos retrieved successfully.", HttpStatus.OK, httpRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getVideoById(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        VideoResponse response = videoService.getVideoById(id);
        return buildResponse(response, "Video found.", HttpStatus.OK, httpRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteVideo(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        videoService.deleteVideo(id);
        return buildResponse(null, "Video deleted successfully.", HttpStatus.OK, httpRequest);
    }
}