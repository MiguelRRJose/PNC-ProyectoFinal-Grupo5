package com.example.cursoapp.service.content.impl;

import com.example.cursoapp.domain.entity.identity.Usuario;
import com.example.cursoapp.domain.entity.content.Answer;
import com.example.cursoapp.domain.entity.content.Question;
import com.example.cursoapp.dto.content.answer.AnswerResponse;
import com.example.cursoapp.dto.content.answer.CreateAnswerRequest;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.content.AnswerMapper;
import com.example.cursoapp.repository.identity.UsuarioRepository;
import com.example.cursoapp.repository.content.AnswerRepository;
import com.example.cursoapp.repository.content.QuestionRepository;
import com.example.cursoapp.service.content.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UsuarioRepository usuarioRepository;
    private final AnswerMapper answerMapper;

    @Override
    @Transactional
    public AnswerResponse createAnswer(CreateAnswerRequest request, Long instructorId) {
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + request.getQuestionId()));
        Usuario instructor = usuarioRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + instructorId));
        Answer answer = Answer.builder()
                .content(request.getContent())
                .question(question)
                .instructor(instructor)
                .creationDate(LocalDateTime.now())
                .build();
        return answerMapper.toDto(answerRepository.save(answer));
    }

    @Override
    public List<AnswerResponse> getAnswersByQuestion(Long questionId) {
        return answerRepository.findByQuestionId(questionId)
                .stream().map(answerMapper::toDto).toList();
    }

    @Override
    public AnswerResponse getAnswerById(Long id) {
        return answerMapper.toDto(answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found with id: " + id)));
    }

    @Override
    @Transactional
    public void deleteAnswer(Long id) {
        answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found with id: " + id));
        answerRepository.deleteById(id);
    }
}