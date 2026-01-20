package com.vbotelho.book_network.services.mapper;

import com.vbotelho.book_network.domain.book.Book;
import com.vbotelho.book_network.domain.feedback.Feedback;
import com.vbotelho.book_network.services.dto.FeedbackRequest;
import com.vbotelho.book_network.services.dto.FeedbackResponse;

import java.util.Objects;

public class FeedbackMapper {

    private FeedbackMapper() { }

    public static Feedback toFeedback(FeedbackRequest request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .book(new Book(request.bookId()))
                .build();
    }
    public static FeedbackResponse toFeedbackResponse(Feedback feedback, Long userId) {
        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), userId.intValue()))
                .build();
    }
}
