package com.example.cursoapp.mapper;

import com.example.cursoapp.domain.entity.content.Video;
import com.example.cursoapp.dto.content.video.VideoResponse;
import org.springframework.stereotype.Component;

@Component
public class VideoMapper {
    public VideoResponse toResponse(Video video) {
        return VideoResponse.builder()
                .id(video.getId())
                .lectionId(video.getLection().getId())
                .videoUrl(video.getVideoUrl())
                .build();
    }
}