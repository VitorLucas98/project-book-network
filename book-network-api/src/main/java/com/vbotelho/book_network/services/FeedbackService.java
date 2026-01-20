package com.vbotelho.book_network.services;

import com.vbotelho.book_network.domain.feedback.Feedback;
import com.vbotelho.book_network.domain.feedback.FeedbackRepository;
import com.vbotelho.book_network.services.dto.FeedbackRequest;
import com.vbotelho.book_network.services.mapper.FeedbackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedBackRepository;
    private final BookService bookService;

    public Long save(FeedbackRequest request, Authentication connectedUser) {
        bookService.validateBook(request.bookId(), connectedUser);
        Feedback feedback = FeedbackMapper.toFeedback(request);
        return feedBackRepository.save(feedback).getId();
    }
}
