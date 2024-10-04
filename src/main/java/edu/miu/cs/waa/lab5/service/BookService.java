package edu.miu.cs.waa.lab5.service;

import edu.miu.cs.waa.lab5.model.Book;
import edu.miu.cs.waa.lab5.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<Book> getBookById(int id) {
        return bookRepository.findById(id);
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(int id, Book book) {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    existingBook.setTitle(book.getTitle());
                    existingBook.setIsbn(book.getIsbn());
                    existingBook.setPrice(book.getPrice());
                    return bookRepository.save(existingBook);
                }).orElse(null);
    }

    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }
}