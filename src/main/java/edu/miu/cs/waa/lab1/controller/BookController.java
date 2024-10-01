package edu.miu.cs.waa.lab1.controller;

import edu.miu.cs.waa.lab1.model.Book;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    private final List<Book> books = new ArrayList<>();

    // Constructor to add some initial books
    public BookController() {
        books.add(new Book(1, "WAA", 3213, 22.5));
        books.add(new Book(2, "ASD", 3214, 29.8));
    }

    // 1. GET /books - Return a list of books
    @GetMapping(produces = "application/cs.miu.edu-v1+json")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(books);
    }

    // 2. GET /books/{id} - Return a single book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable int id, @RequestParam(value = "version", required = false) String version) {
        Optional<Book> book = books.stream()
                .filter(b -> b.getId() == id)
                .findFirst();
        return book.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. POST /books - Add a new book
    @PostMapping(headers = "X-API-VERSION=2")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        int newId = books.stream().mapToInt(Book::getId).max().orElse(0) + 1;
        book.setId(newId);
        books.add(book);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    // 4. Update a book by id
    @PutMapping(value = "/v1/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable int id, @RequestBody Book updatedBook) {
        Optional<Book> existingBook = books.stream()
                .filter(b -> b.getId() == id)
                .findFirst();

        if (existingBook.isPresent()) {
            existingBook.get().setTitle(updatedBook.getTitle());
            existingBook.get().setIsbn(updatedBook.getIsbn());
            existingBook.get().setPrice(updatedBook.getPrice());
            return new ResponseEntity<>(existingBook.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. DELETE /books/{id} - Delete a book by id
    @DeleteMapping(value = "/{id}", produces = "application/cs.miu.edu-v3+json")
    public ResponseEntity<Void> deleteBook(@PathVariable int id) {
        boolean removed = books.removeIf(b -> b.getId() == id);
        if (removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}