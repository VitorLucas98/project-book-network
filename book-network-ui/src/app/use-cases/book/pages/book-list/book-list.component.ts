import { Component, OnInit } from '@angular/core';
import { BookService } from 'src/app/services/book.service';
import { PageResponseBookResponse } from 'src/app/services/models';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements OnInit {

  page: number = 0;
  size: number = 10;

  books: PageResponseBookResponse = {};

  constructor(private bookService: BookService) { }

  ngOnInit(): void {
    this.findAllBooks();
  }

  private findAllBooks(): void {
    this.bookService.findAll(this.page, this.size).subscribe({
      next: (response) => {
        this.books = response;
      },
      error: (error) => {
        console.error('Error fetching books:', error);
      }
    });
  }   
}
