import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PageResponseBookResponse } from './models';
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
  borrowBook(bookId: number): Observable<void> {
    return this.http.post<void>(`${BASE_URL}/borrow/${bookId}`, {});
  }
}
