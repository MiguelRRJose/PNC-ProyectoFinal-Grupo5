package com.example.cursoapp.service;

import com.example.cursoapp.dto.request.CreatePurchaseRequest;
import com.example.cursoapp.dto.response.PurchaseResponse;

import java.util.List;

public interface PurchaseService {
    PurchaseResponse createPurchase(CreatePurchaseRequest request, Long userId);
    List<PurchaseResponse> getPurchasesByUser(Long userId);
    List<PurchaseResponse> getPurchasesByCourse(Long courseId);
    PurchaseResponse getPurchaseById(Long id);
}