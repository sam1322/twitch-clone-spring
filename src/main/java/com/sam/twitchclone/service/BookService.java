package com.sam.twitchclone.service;

import com.sam.twitchclone.controller.book.dto.BookRequest;
import com.sam.twitchclone.dao.postgres.BookRepository;
import com.sam.twitchclone.dao.postgres.model.book.Book;
import com.sam.twitchclone.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public BaseResponse updateById(Long id, BookRequest bookRequest) {
        Optional<Book> book = bookRepository.findById(id);
        String message = "No Book by this Id found";
        if (book.isPresent()) {
            Book book1 = book.get();
            long currentTime = System.currentTimeMillis();
            book1.setTitle(bookRequest.getTitle());
            book1.setUpdatedTime(currentTime);
            bookRepository.save(book1);

            message = "Book updated";
        }

        return BaseResponse.builder()
                .message(message)
                .build();
    }

    public Book save(Book book) {
        long currentTime = System.currentTimeMillis();
        book.setCreatedTime(currentTime);
        book.setUpdatedTime(currentTime);
        return bookRepository.save(book);
    }

    public BaseResponse deleteById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        String message = "No Book by this Id found";
        if (book.isPresent()) {
            bookRepository.deleteById(id);
            message = "Book successfully deleted";
        }
        return BaseResponse.builder()
                .message(message)
                .build();
    }

    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

}
