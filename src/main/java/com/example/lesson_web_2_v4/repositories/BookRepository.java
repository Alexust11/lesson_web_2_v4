package com.example.lesson_web_2_v4.repositories;

import com.example.lesson_web_2_v4.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {
}
