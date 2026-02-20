import { Component, OnInit } from '@angular/core';
import { BookService } from 'src/app/services/book.service';
import { BookResponse, PageResponseBookResponse } from 'src/app/services/models';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements OnInit {

  page: number = 0;
  size: number = 5;

  books: PageResponseBookResponse = {};
   pages: any = [];

  message = '';
  level: 'success' |'error' = 'success';

  constructor(private bookService: BookService) { }

  ngOnInit(): void {
    this.findAllBooks();
  }

  private findAllBooks(): void {
    this.bookService.findAll(this.page, this.size).subscribe({
      next: (response) => {
        this.books = response;
        this.pages = Array(this.books.totalPages)
          .fill(0)
          .map((x, i) => i);
      },
      error: (error) => {
        console.error('Error fetching books:', error);
      }
    });
  }   

  gotToPage(page: number) {
    this.page = page;
    this.findAllBooks();
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBooks();
  }

  goToPreviousPage() {
    this.page --;
    this.findAllBooks();
  }

  goToLastPage() {
    this.page = this.books.totalPages as number - 1;
    this.findAllBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllBooks();
  }

  get isLastPage() {
    return this.page === this.books.totalPages as number - 1;
  }

   borrowBook(book: BookResponse) {
    this.message = '';
    this.level = 'success';
    this.bookService.borrowBook(book.id as number).subscribe({
      next: () => {
        this.level = 'success';
        this.message = 'Book successfully added to your list';
      },
      error: (err) => {
        console.log(err);
        this.level = 'error';
        this.message = err.error.error;
      }
    });
  }
}
