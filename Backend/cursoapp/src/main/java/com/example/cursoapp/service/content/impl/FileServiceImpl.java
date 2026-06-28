package com.example.cursoapp.service.content.impl;

import com.example.cursoapp.domain.entity.content.File;
import com.example.cursoapp.domain.entity.content.Lection;
import com.example.cursoapp.dto.content.file.CreateFileRequest;
import com.example.cursoapp.dto.content.file.FileResponse;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.content.FileMapper;
import com.example.cursoapp.repository.content.FileRepository;
import com.example.cursoapp.repository.content.LectionRepository;
import com.example.cursoapp.service.content.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final LectionRepository lectionRepository;
    private final FileMapper fileMapper;

    @Override
    @Transactional
    public FileResponse createFile(CreateFileRequest request) {
        Lection lection = lectionRepository.findById(request.getLectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Lection not found with id: " + request.getLectionId()));
        File file = File.builder()
                .pathToFile(request.getPathToFile())
                .lection(lection)
                .build();
        return fileMapper.toDto(fileRepository.save(file));
    }

    @Override
    public List<FileResponse> getFilesByLection(Long lectionId) {
        return fileRepository.findByLectionId(lectionId)
                .stream().map(fileMapper::toDto).toList();
    }

    @Override
    public FileResponse getFileById(Long id) {
        return fileMapper.toDto(fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id)));
    }

    @Override
    @Transactional
    public void deleteFile(Long id) {
        fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id));
        fileRepository.deleteById(id);
    }
}