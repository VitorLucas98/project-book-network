package com.vbotelho.book_network.controllers.exception;

import lombok.Builder;

import java.util.Map;
import java.util.Set;

@Builder
public record ExceptionResponse(
        Integer businessErrorCode,
        String businessErrorDescription,
        String error,
        Set<String> validationErrors,
        Map<String, String>errors) {
}
