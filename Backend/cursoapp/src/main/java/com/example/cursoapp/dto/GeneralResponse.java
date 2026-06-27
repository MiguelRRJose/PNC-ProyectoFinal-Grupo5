package com.example.cursoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponse<T> {
    private String message;
    private T data;

    public static <T> GeneralResponse<T> of(String message, T data) {
        return GeneralResponse.<T>builder().message(message).data(data).build();
    }
    public static <T> GeneralResponse<T> of(String message) {
        return GeneralResponse.<T>builder().message(message).build();
    }
}