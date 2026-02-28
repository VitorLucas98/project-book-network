import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BookRequest, BookResponse, PageResponseBookResponse, PageResponseBorrowedBookResponse } from './models';
import { Observable } from 'rxjs';

const BASE_URL = 'http://localhost:8081/api/v1/books';


@Injectable({
  providedIn: 'root'
})
export class BookService {

  constructor(private http: HttpClient) { }

  findAll(page: number, size: number): Observable<PageResponseBookResponse> {
    return this.http.get<PageResponseBookResponse>(`${BASE_URL}?page=${page}&size=${size}`);
  }
  
  findAllBooksByOwner(page: number, size: number): Observable<PageResponseBookResponse> {
    return this.http.get<PageResponseBookResponse>(`${BASE_URL}/owner?page=${page}&size=${size}`);
  }

  borrowBook(bookId: number): Observable<void> {
    return this.http.post<void>(`${BASE_URL}/borrow/${bookId}`, {});
  }

  saveBook(bookRequest: BookRequest): Observable<number> {
    return this.http.post<number>(`${BASE_URL}`, bookRequest);
  }

  uploadBookCoverPicture(bookId: number, file: File): Observable<void> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<void>(`${BASE_URL}/cover/${bookId}`, formData);
  }

  findBookById(bookId: number): Observable<BookResponse> {
    return this.http.get<BookResponse>(`${BASE_URL}/${bookId}`);
  }
  
  updateShareableStatus(bookId: number): Observable<void> {
    return this.http.patch<void>(`${BASE_URL}/shareable/${bookId}`, {});
  }

  updateArchivedStatus(bookId: number): Observable<void> {
    return this.http.patch<void>(`${BASE_URL}/archived/${bookId}`, {});
  }

  findAllBorrowedBooks(page: number, size: number): Observable<PageResponseBorrowedBookResponse> {
    return this.http.get<PageResponseBorrowedBookResponse>(`${BASE_URL}/borrowed?page=${page}&size=${size}`);
  }

  findAllReturnedBooks(page: number, size: number): Observable<PageResponseBorrowedBookResponse> {
    return this.http.get<PageResponseBorrowedBookResponse>(`${BASE_URL}/returned?page=${page}&size=${size}`);
  }

  returnBorrowBook(bookId: number): Observable<void> {
    return this.http.patch<void>(`${BASE_URL}/borrow/return/${bookId}`, {});
  }

  approveReturnBorrowBook(bookId: number): Observable<void> {
    return this.http.patch<void>(`${BASE_URL}/borrow/return/approve/${bookId}`, {});
  }
  
}
