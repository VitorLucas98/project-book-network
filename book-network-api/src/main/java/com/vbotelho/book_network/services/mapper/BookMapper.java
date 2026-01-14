package com.vbotelho.book_network.services.mapper;

import com.vbotelho.book_network.domain.book.Book;
import com.vbotelho.book_network.services.dto.BookRequest;

public class BookMapper {
    public static Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .isbn(request.isbn())
                .authorName(request.authorName())
                .synopsis(request.synopsis())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }
}
