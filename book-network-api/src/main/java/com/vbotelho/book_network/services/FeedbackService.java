package com.vbotelho.book_network.services;

import com.vbotelho.book_network.domain.feedback.Feedback;
import com.vbotelho.book_network.domain.feedback.FeedbackRepository;
import com.vbotelho.book_network.domain.user.User;
import com.vbotelho.book_network.services.dto.FeedbackRequest;
import com.vbotelho.book_network.services.dto.FeedbackResponse;
import com.vbotelho.book_network.services.dto.PageResponse;
import com.vbotelho.book_network.services.mapper.FeedbackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public PageResponse<FeedbackResponse> findAllFeedbacksByBook(Long bookId, int page,
                                                                 int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = ((User) connectedUser.getPrincipal());
        Page<Feedback> feedbacks = feedBackRepository.findAllByBookId(bookId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(f -> FeedbackMapper.toFeedbackResponse(f, user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
