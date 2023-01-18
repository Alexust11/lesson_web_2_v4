package com.example.lesson_web_2_v4.repositories;

import com.example.lesson_web_2_v4.model.Book;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Collections;

public interface BookRepository extends JpaRepository<Book,Long> {
    Book findByName(String name);

    Collection<Book> findBooksByAuthor(String author);

    Collection<Book> findAllByNameContains(String part);
}
