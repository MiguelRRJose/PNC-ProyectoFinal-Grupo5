package com.example.cursoapp.service;

import com.example.cursoapp.dto.request.CreateFileRequest;
import com.example.cursoapp.dto.response.FileResponse;

import java.util.List;

public interface FileService {
    FileResponse createFile(CreateFileRequest request);
    List<FileResponse> getFilesByLection(Long lectionId);
    FileResponse getFileById(Long id);
    void deleteFile(Long id);
}