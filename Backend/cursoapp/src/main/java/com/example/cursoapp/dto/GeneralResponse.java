package com.example.cursoapp.dto;

public record GeneralResponse (
    Object data,
    String message,
    Integer statusCode
) {
}