package com.example.lesson_web_2_v4.controllers;

import com.example.lesson_web_2_v4.model.Book;
import com.example.lesson_web_2_v4.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/books")
public class BooksController {
    public BookService bookService;

    public BooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping//http://localhost:8080/books/
    public Book creatBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    @GetMapping({"id"}) //http://localhost:8080/books/1
    public ResponseEntity<Book> getBookInfo(@PathVariable long id) {
        Book book = bookService.findBook(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }
    @PutMapping
    public ResponseEntity<Book> editBook(@RequestBody Book book) {
        Book foundBook = bookService.editBook(book);
        if (foundBook == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundBook) ;
    }
    @DeleteMapping("{id}")
    public void  deleteStudent(@PathVariable long id) {
        bookService.deleteBook(id);

    }
    @GetMapping()
    public ResponseEntity<Collection<Book>> getAllBook() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }


}
