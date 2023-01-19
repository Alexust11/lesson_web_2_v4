package com.example.lesson_web_2_v4.repositories;

import com.example.lesson_web_2_v4.model.Book;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Collections;

public interface BookRepository extends JpaRepository<Book,Long> {
    Book findByNameIgnoreCase(String name);

    Book findBookById(Long id);

    Collection<Book> findBooksByAuthorContainsIgnoreCase(String author);

    Collection<Book> findAllByNameContainsIgnoreCase(String part);

    Collection<Book> findBooksByNameAndAuthor(String name, String author);
}
