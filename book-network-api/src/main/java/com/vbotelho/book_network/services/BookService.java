package com.vbotelho.book_network.services;

import com.vbotelho.book_network.domain.book.Book;
import com.vbotelho.book_network.domain.book.BookRepository;
import com.vbotelho.book_network.domain.user.User;
import com.vbotelho.book_network.services.dto.BookRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.vbotelho.book_network.services.mapper.BookMapper.toBook;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public Long save(BookRequest request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Book book = toBook(request);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }
}
