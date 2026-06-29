package com.example.cursoapp.controller.content;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.dto.content.question.CreateQuestionRequest;
import com.example.cursoapp.dto.content.question.QuestionResponse;
import com.example.cursoapp.service.content.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

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

    @PostMapping("/user/{userId}")
    public ResponseEntity<GeneralResponse> createQuestion(@Valid @RequestBody CreateQuestionRequest request,
                                                          @PathVariable Long userId) {
        return buildResponse(questionService.createQuestion(request, userId), "Question created successfully.", HttpStatus.CREATED);
    }

    @GetMapping("/lection/{lectionId}")
    public ResponseEntity<GeneralResponse> getQuestionsByLection(@PathVariable Long lectionId) {
        return buildResponse(questionService.getQuestionsByLection(lectionId), "Questions retrieved successfully.", HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<GeneralResponse> getQuestionsByUser(@PathVariable Long userId) {
        return buildResponse(questionService.getQuestionsByUser(userId), "Questions retrieved successfully.", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getQuestionById(@PathVariable Long id) {
        return buildResponse(questionService.getQuestionById(id), "Question found.", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return buildResponse(null, "Question deleted successfully.", HttpStatus.OK);
    }
}