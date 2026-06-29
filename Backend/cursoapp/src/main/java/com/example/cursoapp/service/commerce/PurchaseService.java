package com.example.cursoapp.service.commerce;

import com.example.cursoapp.dto.commerce.purchase.CreatePurchaseRequest;
import com.example.cursoapp.dto.commerce.purchase.PurchaseResponse;

import java.util.List;

public interface PurchaseService {
    PurchaseResponse getPurchaseById(Long id);
    List<PurchaseResponse> getPurchasesByUser(Long userId);
    List<PurchaseResponse> getPurchasesByCourse(Long courseId);
    List<PurchaseResponse> getAllPurchases();
    PurchaseResponse createPurchase(CreatePurchaseRequest request, Long userId);
}
