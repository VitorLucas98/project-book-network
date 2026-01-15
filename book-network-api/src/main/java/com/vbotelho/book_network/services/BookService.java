package com.vbotelho.book_network.services;

import com.vbotelho.book_network.domain.book.Book;
import com.vbotelho.book_network.domain.book.BookRepository;
import com.vbotelho.book_network.domain.history.BookTransactionHistory;
import com.vbotelho.book_network.domain.history.BookTransactionHistoryRepository;
import com.vbotelho.book_network.domain.user.User;
import com.vbotelho.book_network.services.dto.BookRequest;
import com.vbotelho.book_network.services.dto.BookResponse;
import com.vbotelho.book_network.services.dto.BorrowedBookResponse;
import com.vbotelho.book_network.services.dto.PageResponse;
import com.vbotelho.book_network.services.exception.OperationNotPermittedException;
import com.vbotelho.book_network.services.mapper.BookMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.vbotelho.book_network.domain.book.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository transactionHistoryRepository;

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

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> booksResponse = books.stream()
                .map(BookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                booksResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll(withOwnerId(user.getId()), pageable);
        List<BookResponse> booksResponse = books.stream()
                .map(BookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                booksResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }


    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = transactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());

        List<BorrowedBookResponse> booksResponse = allBorrowedBooks.stream()
                .map(BookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                booksResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
         User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = transactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());
        List<BorrowedBookResponse> booksResponse = allBorrowedBooks.stream()
                .map(BookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                booksResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public Long updateShareableStatus(Long bookId, Authentication connectedUser) {
        Book book = findBookById(bookId);
        User user = ((User) connectedUser.getPrincipal());
        validateBookOwnerIsAuthenticatedUser(book.getOwner().getId(), user.getId());
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    public Long updateArchivedStatus(Long bookId, Authentication connectedUser) {
        Book book = findBookById(bookId);
        User user = ((User) connectedUser.getPrincipal());
        validateBookOwnerIsAuthenticatedUser(book.getOwner().getId(), user.getId());
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    public Long borrowBook(Long bookId, Authentication connectedUser) {
        Book book = findBookById(bookId);
        validateBookAvailability(book);
        User user = ((User) connectedUser.getPrincipal());
        validateBookOwnerIsAuthenticatedUser(book.getOwner().getId(), user.getId());
        final boolean isAlreadyBorrowed = transactionHistoryRepository.isAlreadyBorrowed(bookId, user.getId());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("Te requested book is already borrowed");
        }

        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return transactionHistoryRepository.save(bookTransactionHistory).getId();

    }

    public Long returnBorrowedBook(Long bookId, Authentication connectedUser) {
        Book book = findBookById(bookId);
        validateBookAvailability(book);
        User user = ((User) connectedUser.getPrincipal());
        validateBookOwnerIsAuthenticatedUser(book.getOwner().getId(), user.getId());

        BookTransactionHistory bookTransactionHistory = transactionHistoryRepository.findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book"));

        bookTransactionHistory.setReturned(true);
        return transactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    private  void validateBookOwnerIsAuthenticatedUser(Long idOwner, Long idUser){
        if(!Objects.equals(idOwner, idUser)){
            throw new OperationNotPermittedException("You cannot update others books shareable status");
        }
    }

    private Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + bookId));
    }

    private void validateBookAvailability (Book book){
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }
    }
}
