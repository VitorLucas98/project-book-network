package com.vbotelho.book_network.services;

import com.vbotelho.book_network.domain.book.Book;
import com.vbotelho.book_network.domain.book.BookRepository;
import com.vbotelho.book_network.domain.user.User;
import com.vbotelho.book_network.services.dto.BookRequest;
import com.vbotelho.book_network.services.dto.BookResponse;
import com.vbotelho.book_network.services.mapper.BookMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public Long save(BookRequest request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Book book = BookMapper.toBook(request);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    public BookResponse findById(Long bookId) {
        return bookRepository.findById(bookId)
                .map(BookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + bookId));
    }
}
