package com.vbotelho.book_network.services.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FeedbackRequest(
        @Positive(message = "Note must be a positive value")
        @Min(value = 0, message = "Note must be at least 0")
        @Max(value = 5, message = "Note must be at most 5")
        Double note,
        @NotBlank(message = "Comment is mandatory")
        String comment,
        @NotNull(message = "Book ID is mandatory")
        Long bookId) {
}