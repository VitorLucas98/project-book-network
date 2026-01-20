package com.vbotelho.book_network.services.mapper;

import com.vbotelho.book_network.domain.book.Book;
import com.vbotelho.book_network.domain.feedback.Feedback;
import com.vbotelho.book_network.services.dto.FeedbackRequest;

public class FeedbackMapper {

    private FeedbackMapper() { }

    public static Feedback toFeedback(FeedbackRequest request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .book(new Book(request.bookId()))
                .build();
    }
}
