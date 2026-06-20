package com.example.cursoapp.controller;

import com.example.cursoapp.dto.content.answer.CreateAnswerRequest;
import com.example.cursoapp.dto.content.answer.AnswerResponse;
import com.example.cursoapp.dto.response.GeneralResponse;
import com.example.cursoapp.service.impl.AnswerServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerServiceImpl answerService;

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

    @PostMapping("/instructor/{instructorId}")
    public ResponseEntity<GeneralResponse> createAnswer(
            @Valid @RequestBody CreateAnswerRequest request,
            @PathVariable Long instructorId,
            HttpServletRequest httpRequest) {
        AnswerResponse response = answerService.createAnswer(request, instructorId);
        return buildResponse(response, "Answer created successfully.", HttpStatus.CREATED, httpRequest);
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<GeneralResponse> getAnswersByQuestion(
            @PathVariable Long questionId,
            HttpServletRequest httpRequest) {
        List<AnswerResponse> response = answerService.getAnswersByQuestion(questionId);
        return buildResponse(response, "Answers retrieved successfully.", HttpStatus.OK, httpRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getAnswerById(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        AnswerResponse response = answerService.getAnswerById(id);
        return buildResponse(response, "Answer found.", HttpStatus.OK, httpRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteAnswer(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        answerService.deleteAnswer(id);
        return buildResponse(null, "Answer deleted successfully.", HttpStatus.OK, httpRequest);
    }
}