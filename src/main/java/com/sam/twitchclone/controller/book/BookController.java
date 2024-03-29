package com.sam.twitchclone.controller.book;

import com.sam.twitchclone.controller.book.dto.BookRequest;
import com.sam.twitchclone.dao.postgres.model.book.Book;
import com.sam.twitchclone.model.BaseResponse;
import com.sam.twitchclone.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    @GetMapping("/v1/book/all")
    public ResponseEntity<List<Book>> findAllBooks(){
        return ResponseEntity.ok().body(bookService.findAll());
    }
    @GetMapping("/v1/book/{id}")
    public ResponseEntity<Book> finBookById(@PathVariable Long id) {
        return ResponseEntity.ok().body(bookService.findById(id).get());
    }

    @PutMapping("/v1/book/{id}")
    public ResponseEntity<BaseResponse>  updateBookById(@PathVariable Long id, @RequestBody BookRequest bookRequest) {
        return ResponseEntity.ok().body(bookService. updateById(id,bookRequest));
    }

    @GetMapping("/v1/book/title")
    public ResponseEntity<List<Book>> findBookByTitle(@RequestBody BookRequest bookRequest) {
        return ResponseEntity.ok().body(bookService.findByTitle(bookRequest.getTitle()));
    }

//    @ResponseStatus(HttpStatus.CREATED) // 201
    @PostMapping("/v1/book")
    public ResponseEntity<Book>  create(@RequestBody Book book) {
        return ResponseEntity.ok().body(bookService.save(book));
    }

    @DeleteMapping("/v1/book/{id}")
    public ResponseEntity<BaseResponse> deleteBookById(@PathVariable Long id) {
        BaseResponse baseResponse = bookService.deleteById(id);
        return ResponseEntity.ok().body(baseResponse);
    }

}
