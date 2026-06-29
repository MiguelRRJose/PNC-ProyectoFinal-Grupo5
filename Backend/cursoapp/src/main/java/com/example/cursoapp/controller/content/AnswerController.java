package com.example.cursoapp.controller.content;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.dto.content.answer.CreateAnswerRequest;
import com.example.cursoapp.dto.content.answer.AnswerResponse;
import com.example.cursoapp.service.content.AnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

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

    @PostMapping("/instructor/{instructorId}")
    public ResponseEntity<GeneralResponse> createAnswer(
            @Valid @RequestBody CreateAnswerRequest request,
            @PathVariable Long instructorId) {
        return buildResponse(answerService.createAnswer(request, instructorId), "Answer created successfully.", HttpStatus.CREATED);
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<GeneralResponse> getAnswersByQuestion(@PathVariable Long questionId) {
        return buildResponse(answerService.getAnswersByQuestion(questionId), "Answers retrieved successfully.", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getAnswerById(@PathVariable Long id) {
        return buildResponse(answerService.getAnswerById(id), "Answer found.", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteAnswer(@PathVariable Long id) {
        answerService.deleteAnswer(id);
        return buildResponse(null, "Answer deleted successfully.", HttpStatus.OK);
    }
}