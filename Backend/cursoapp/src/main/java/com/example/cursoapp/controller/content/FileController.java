package com.example.cursoapp.controller.content;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.dto.content.file.CreateFileRequest;
import com.example.cursoapp.dto.content.file.FileResponse;
import com.example.cursoapp.service.content.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

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
    public ResponseEntity<GeneralResponse> createFile(@Valid @RequestBody CreateFileRequest request) {
        return buildResponse(fileService.createFile(request), "File created successfully.", HttpStatus.CREATED);
    }

    @GetMapping("/lection/{lectionId}")
    public ResponseEntity<GeneralResponse> getFilesByLection(@PathVariable Long lectionId) {
        return buildResponse(fileService.getFilesByLection(lectionId), "Files retrieved successfully.", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getFileById(@PathVariable Long id) {
        return buildResponse(fileService.getFileById(id), "File found.", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return buildResponse(null, "File deleted successfully.", HttpStatus.OK);
    }
}