package com.example.cursoapp.mapper.content;

import com.example.cursoapp.domain.entity.content.Video;
import com.example.cursoapp.dto.content.video.VideoResponse;
import org.springframework.stereotype.Component;

@Component
public class VideoMapper {

    public VideoResponse toDto(Video video) {
        return VideoResponse.builder()
                .id(video.getId())
                .videoUrl(video.getVideoUrl())
                .lectionId(video.getLection().getId())
                .build();
    }
}