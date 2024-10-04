package edu.miu.cs.waa.lab5.controller;

import edu.miu.cs.waa.lab5.model.Book;
import edu.miu.cs.waa.lab5.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // GET /books - Return a list of books
    @GetMapping(produces = "application/cs.miu.edu-v1+json")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    // GET /books/{id} - Return a single book by ID
    @GetMapping(value = "/{id}", headers = "X-API-VERSION=1")
    public ResponseEntity<Book> getBookById(@PathVariable int id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // POST /books - Add a new book
    @PostMapping(headers = "X-API-VERSION=2")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    // PUT /books/{id} - Update a book by id
    @PutMapping(value = "/{id}", headers = "X-API-VERSION=1")
    public ResponseEntity<Book> updateBook(@PathVariable int id, @RequestBody Book updatedBook) {
        Book book = bookService.updateBook(id, updatedBook);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(book);
    }

    // DELETE /books/{id} - Delete a book by id
    @DeleteMapping(value = "/{id}", headers = "X-API-VERSION=3")
    public ResponseEntity<Void> deleteBook(@PathVariable int id) {
        if (bookService.getBookById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}