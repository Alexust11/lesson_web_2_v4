package com.example.lesson_web_2_v4.service;

import com.example.lesson_web_2_v4.model.Book;
import com.example.lesson_web_2_v4.model.BookCover;
import com.example.lesson_web_2_v4.repositories.BookCoverRepository;
import com.example.lesson_web_2_v4.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class BookCoverService {
@Value("covers")
    private String coversDid;
    private final BookService bookService;
    private final BookCoverRepository bookCoverRepository;

    public BookCoverService(BookService bookService, BookCoverRepository bookCoverRepository) {
        this.bookService = bookService;
        this.bookCoverRepository = bookCoverRepository;
    }


    public void uploadCover(long bookId, MultipartFile file) throws IOException {
        Book book = bookService.findBook(bookId);
        Path filePath=Path.of(coversDid,bookId+"."+getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        BookCover bookCover=findBookCover(bookId);
        bookCover.setBook(book);
        bookCover.setFilePath(filePath.toString());
        bookCover.setFileSize(file.getSize());
        bookCover.setMediaType(file.getContentType());
        bookCover.setPreview(generalImagePreview(filePath));
        bookCoverRepository.save(bookCover);
    }

    private byte[] generalImagePreview(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis=new BufferedInputStream(is,1024);
             ByteArrayOutputStream baos=new ByteArrayOutputStream())
        {
            BufferedImage image= ImageIO.read(bis);
            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics=preview.createGraphics();
            graphics.drawImage(image,0,0,100,height,null);
            graphics.dispose();
            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    private BookCover findBookCover(long bookId) {
        return bookCoverRepository.findBookById(bookId).orElse(new BookCover());
    }

    private String getExtension(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(".")+1);
    }
}
