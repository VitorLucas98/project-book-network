package com.vbotelho.book_network.services.dto;

import jakarta.validation.constraints.NotBlank;

public record BookRequest(Long id,
                          @NotBlank(message = "Title is mandatory")
                          String title,
                          @NotBlank(message = "Author name is mandatory")
                          String authorName,
                          @NotBlank(message = "ISBN is mandatory")
                          String isbn,
                          @NotBlank(message = "Synopsis is mandatory")
                          String synopsis,
                          boolean shareable) {
}
