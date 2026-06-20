package com.example.cursoapp.service;

import com.example.cursoapp.dto.content.video.CreateVideoRequest;
import com.example.cursoapp.dto.content.video.VideoResponse;

import java.util.List;

public interface VideoService {
    VideoResponse createVideo(CreateVideoRequest request);
    List<VideoResponse> getVideosByLection(Long lectionId);
    VideoResponse getVideoById(Long id);
    void deleteVideo(Long id);
}