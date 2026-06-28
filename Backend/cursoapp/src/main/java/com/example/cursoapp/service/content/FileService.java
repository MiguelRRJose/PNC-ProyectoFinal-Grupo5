package com.example.cursoapp.service.content;

import com.example.cursoapp.dto.content.file.CreateFileRequest;
import com.example.cursoapp.dto.content.file.FileResponse;

import java.util.List;

public interface FileService {
    FileResponse createFile(CreateFileRequest request);
    List<FileResponse> getFilesByLection(Long lectionId);
    FileResponse getFileById(Long id);
    void deleteFile(Long id);
}