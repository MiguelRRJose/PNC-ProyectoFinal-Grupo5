package com.example.cursoapp.service.content.impl;

import com.example.cursoapp.domain.entity.content.Lection;
import com.example.cursoapp.domain.entity.content.Video;
import com.example.cursoapp.dto.content.video.CreateVideoRequest;
import com.example.cursoapp.dto.content.video.VideoResponse;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.content.VideoMapper;
import com.example.cursoapp.repository.content.LectionRepository;
import com.example.cursoapp.repository.content.VideoRepository;
import com.example.cursoapp.service.content.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final LectionRepository lectionRepository;
    private final VideoMapper videoMapper;

    @Override
    @Transactional
    public VideoResponse createVideo(CreateVideoRequest request) {
        Lection lection = lectionRepository.findById(request.getLectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Lection not found with id: " + request.getLectionId()));
        Video video = Video.builder()
                .videoUrl(request.getVideoUrl())
                .lection(lection)
                .build();
        return videoMapper.toDto(videoRepository.save(video));
    }

    @Override
    public List<VideoResponse> getVideosByLection(Long lectionId) {
        return videoRepository.findByLectionId(lectionId)
                .stream().map(videoMapper::toDto).toList();
    }

    @Override
    public VideoResponse getVideoById(Long id) {
        return videoMapper.toDto(videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + id)));
    }

    @Override
    @Transactional
    public void deleteVideo(Long id) {
        videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + id));
        videoRepository.deleteById(id);
    }
}