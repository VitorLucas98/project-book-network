package com.vbotelho.book_network.services.mapper;

import com.vbotelho.book_network.domain.book.Book;
import com.vbotelho.book_network.services.dto.BookRequest;
import com.vbotelho.book_network.services.dto.BookResponse;

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

    public static BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .rate(book.getRate())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .owner(book.getOwner().getFullName())
                //.cover(FileUtils.readFileFromLocation(book.getBookCover()))
                .build();
    }
}
