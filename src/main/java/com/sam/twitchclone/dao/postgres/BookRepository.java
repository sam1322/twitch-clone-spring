package com.sam.twitchclone.dao.postgres;

import com.sam.twitchclone.dao.postgres.model.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitle(String title);
}
