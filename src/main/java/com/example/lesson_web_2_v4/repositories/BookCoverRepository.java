package com.example.lesson_web_2_v4.repositories;

import com.example.lesson_web_2_v4.model.BookCover;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookCoverRepository extends JpaRepository<BookCover,Long> {

    Optional<BookCover> findBookById(Long bookId);
}
