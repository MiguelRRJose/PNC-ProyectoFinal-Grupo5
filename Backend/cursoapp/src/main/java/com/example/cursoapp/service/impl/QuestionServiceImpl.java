package com.example.cursoapp.service.impl;

import com.example.cursoapp.domain.entity.content.Lection;
import com.example.cursoapp.domain.entity.content.Question;
import com.example.cursoapp.domain.entity.Usuario;
import com.example.cursoapp.dto.content.question.CreateQuestionRequest;
import com.example.cursoapp.dto.content.question.QuestionResponse;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.QuestionMapper;
import com.example.cursoapp.repository.LectionRepository;
import com.example.cursoapp.repository.QuestionRepository;
import com.example.cursoapp.repository.UsuarioRepository;
import com.example.cursoapp.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final LectionRepository lectionRepository;
    private final UsuarioRepository usuarioRepository;
    private final QuestionMapper questionMapper;

    @Override
    @Transactional
    public QuestionResponse createQuestion(CreateQuestionRequest request, Long userId) {
        Lection lection = lectionRepository.findById(request.getLectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Lection not found with id: " + request.getLectionId()));
        Usuario user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Question question = Question.builder()
                .content(request.getContent())
                .lection(lection)
                .user(user)
                .creationDate(LocalDateTime.now())
                .build();
        return questionMapper.toDto(questionRepository.save(question));
    }

    @Override
    public List<QuestionResponse> getQuestionsByLection(Long lectionId) {
        return questionRepository.findByLectionId(lectionId)
                .stream().map(questionMapper::toDto).toList();
    }

    @Override
    public List<QuestionResponse> getQuestionsByUser(Long userId) {
        return questionRepository.findByUserId(userId)
                .stream().map(questionMapper::toDto).toList();
    }

    @Override
    public QuestionResponse getQuestionById(Long id) {
        return questionMapper.toDto(questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id)));
    }

    @Override
    @Transactional
    public void deleteQuestion(Long id) {
        questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
        questionRepository.deleteById(id);
    }
}