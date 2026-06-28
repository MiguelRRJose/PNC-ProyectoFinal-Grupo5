package com.example.cursoapp.controller;

import com.example.cursoapp.dto.content.question.CreateQuestionRequest;
import com.example.cursoapp.dto.response.GeneralResponse;
import com.example.cursoapp.dto.content.question.QuestionResponse;
import com.example.cursoapp.service.content.impl.QuestionServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionServiceImpl questionService;

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

    @PostMapping("/user/{userId}")
    public ResponseEntity<GeneralResponse> createQuestion(
            @Valid @RequestBody CreateQuestionRequest request,
            @PathVariable Long userId,
            HttpServletRequest httpRequest) {
        QuestionResponse response = questionService.createQuestion(request, userId);
        return buildResponse(response, "Question created successfully.", HttpStatus.CREATED, httpRequest);
    }

    @GetMapping("/lection/{lectionId}")
    public ResponseEntity<GeneralResponse> getQuestionsByLection(
            @PathVariable Long lectionId,
            HttpServletRequest httpRequest) {
        List<QuestionResponse> response = questionService.getQuestionsByLection(lectionId);
        return buildResponse(response, "Questions retrieved successfully.", HttpStatus.OK, httpRequest);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<GeneralResponse> getQuestionsByUser(
            @PathVariable Long userId,
            HttpServletRequest httpRequest) {
        List<QuestionResponse> response = questionService.getQuestionsByUser(userId);
        return buildResponse(response, "Questions retrieved successfully.", HttpStatus.OK, httpRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getQuestionById(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        QuestionResponse response = questionService.getQuestionById(id);
        return buildResponse(response, "Question found.", HttpStatus.OK, httpRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteQuestion(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        questionService.deleteQuestion(id);
        return buildResponse(null, "Question deleted successfully.", HttpStatus.OK, httpRequest);
    }
}