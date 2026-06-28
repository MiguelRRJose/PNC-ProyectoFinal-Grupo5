package com.example.cursoapp.controller;

import com.example.cursoapp.dto.content.module.CreateModuleRequest;
import com.example.cursoapp.dto.content.module.UpdateModuleRequest;
import com.example.cursoapp.dto.response.GeneralResponse;
import com.example.cursoapp.dto.content.module.ModuleResponse;
import com.example.cursoapp.service.content.impl.ModuleServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleServiceImpl moduleService;

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
    public ResponseEntity<GeneralResponse> createModule(
            @Valid @RequestBody CreateModuleRequest request,
            HttpServletRequest httpRequest) {
        ModuleResponse response = moduleService.createModule(request);
        return buildResponse(response, "Module created successfully.", HttpStatus.CREATED, httpRequest);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<GeneralResponse> getModulesByCourse(
            @PathVariable Long courseId,
            HttpServletRequest httpRequest) {
        List<ModuleResponse> response = moduleService.getModulesByCourse(courseId);
        return buildResponse(response, "Modules retrieved successfully.", HttpStatus.OK, httpRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getModuleById(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        ModuleResponse response = moduleService.getModuleById(id);
        return buildResponse(response, "Module found.", HttpStatus.OK, httpRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> updateModule(
            @PathVariable Long id,
            @Valid @RequestBody UpdateModuleRequest request,
            HttpServletRequest httpRequest) {
        ModuleResponse response = moduleService.updateModule(id, request);
        return buildResponse(response, "Module updated successfully.", HttpStatus.OK, httpRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteModule(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        moduleService.deleteModule(id);
        return buildResponse(null, "Module deleted successfully.", HttpStatus.OK, httpRequest);
    }
}