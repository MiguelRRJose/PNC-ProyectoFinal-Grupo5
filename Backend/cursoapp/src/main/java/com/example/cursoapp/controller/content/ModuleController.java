package com.example.cursoapp.controller.content;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.dto.content.module.CreateModuleRequest;
import com.example.cursoapp.dto.content.module.UpdateModuleRequest;
import com.example.cursoapp.dto.content.module.ModuleResponse;
import com.example.cursoapp.service.content.ModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

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
    public ResponseEntity<GeneralResponse> createModule(@Valid @RequestBody CreateModuleRequest request) {
        return buildResponse(moduleService.createModule(request), "Module created successfully.", HttpStatus.CREATED);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<GeneralResponse> getModulesByCourse(@PathVariable Long courseId) {
        return buildResponse(moduleService.getModulesByCourse(courseId), "Modules retrieved successfully.", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getModuleById(@PathVariable Long id) {
        return buildResponse(moduleService.getModuleById(id), "Module found.", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> updateModule(@PathVariable Long id,
                                                        @Valid @RequestBody UpdateModuleRequest request) {
        return buildResponse(moduleService.updateModule(id, request), "Module updated successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteModule(@PathVariable Long id) {
        moduleService.deleteModule(id);
        return buildResponse(null, "Module deleted successfully.", HttpStatus.OK);
    }
}