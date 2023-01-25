package com.example.lesson_web_2_v4.controllers;

import com.example.lesson_web_2_v4.model.Book;
import com.example.lesson_web_2_v4.model.BookCover;
import com.example.lesson_web_2_v4.service.BookCoverService;
import com.example.lesson_web_2_v4.service.BookService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

@RestController
@RequestMapping("/books")
public class BooksController {
    public BookService bookService;
    public BookCoverService bookCoverService;

    public BooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping//http://localhost:8080/books/
    public Book creatBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    @GetMapping("{id}") //http://localhost:8080/books/1
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
        return ResponseEntity.ok(foundBook);
    }

    @DeleteMapping("{id}")
    public void deleteStudent(@PathVariable long id) {
        bookService.deleteBook(id);

    }

    @GetMapping()
    public ResponseEntity findBooks(@RequestParam(required = false) String name,
                                    @RequestParam(required = false) String author,
                                    @RequestParam(required = false) String namePart) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(bookService.findByName(name));
        }
        if (author != null && !author.isBlank()) {
            return ResponseEntity.ok(bookService.findByAuthor(author));
        }
        if (namePart != null && !namePart.isBlank()) {
            return ResponseEntity.ok(bookService.findByNamePart(namePart));
        }
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PostMapping(value = "/{id}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploaderCover(@PathVariable long id, @RequestParam MultipartFile cover) throws IOException {
        if (cover.getSize() >= 1024 * 300) {
            return ResponseEntity.badRequest().body("File is to big");
        }
        bookCoverService.uploadCover(id, cover);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/cover/preview")
    public ResponseEntity<byte[]> downlaodCover(@PathVariable Long id) {
        BookCover bookCover = bookCoverService.findBookCover(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(bookCover.getMediaType()));
        headers.setContentLength(bookCover.getPreview().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(bookCover.getPreview());

    }

    @GetMapping(value = "/{id}/cover")
    public void downloadCover(@PathVariable Long id, HttpServletResponse response) throws IOException {
        BookCover bookCover = bookCoverService.findBookCover(id);
        Path path=Path.of(bookCover.getFilePath());
        try (InputStream is= Files.newInputStream(path);
             OutputStream os=response.getOutputStream();)
        {
            response.setStatus(200);
            response.setContentType(bookCover.getMediaType());
            response.setContentLength((int) bookCover.getFileSize());
            is.transferTo(os);
        }

    }
}
