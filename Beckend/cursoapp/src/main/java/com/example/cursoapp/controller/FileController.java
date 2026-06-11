package com.example.cursoapp.controller;

import com.example.cursoapp.dto.request.CreateFileRequest;
import com.example.cursoapp.dto.response.FileResponse;
import com.example.cursoapp.dto.response.GeneralResponse;
import com.example.cursoapp.service.impl.FileServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileServiceImpl fileService;

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
    public ResponseEntity<GeneralResponse> createFile(
            @Valid @RequestBody CreateFileRequest request,
            HttpServletRequest httpRequest) {
        FileResponse response = fileService.createFile(request);
        return buildResponse(response, "File created successfully.", HttpStatus.CREATED, httpRequest);
    }

    @GetMapping("/lection/{lectionId}")
    public ResponseEntity<GeneralResponse> getFilesByLection(
            @PathVariable Long lectionId,
            HttpServletRequest httpRequest) {
        List<FileResponse> response = fileService.getFilesByLection(lectionId);
        return buildResponse(response, "Files retrieved successfully.", HttpStatus.OK, httpRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getFileById(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        FileResponse response = fileService.getFileById(id);
        return buildResponse(response, "File found.", HttpStatus.OK, httpRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteFile(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        fileService.deleteFile(id);
        return buildResponse(null, "File deleted successfully.", HttpStatus.OK, httpRequest);
    }
}