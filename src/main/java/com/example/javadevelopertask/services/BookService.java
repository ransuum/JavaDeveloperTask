package com.example.javadevelopertask.services;

import com.example.javadevelopertask.model.entity.Book;
import com.example.javadevelopertask.repo.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {
    @Autowired
    private BookRepo bookRepo;

    public Book create(String title, String author, String isbn, Integer quantity) {
        if (isbn != null) {
            return bookRepo.save(Book.builder()
                    .title(title)
                    .author(author)
                    .quantity(quantity)
                    .isbn(isbn)
                    .build());
        }  else {
            return null;
        }
    }
    public Optional<Book> findById(UUID id){
        return bookRepo.findById(id);
    }
    public UUID deleteById(UUID id){
        bookRepo.deleteById(id);
        return id;
    }
    public List<Book> findAuthorsAndDelete(String author){
        List<Book> all = bookRepo.findAll().stream().filter(book -> book.getAuthor().equals(author)).toList();
        bookRepo.deleteAll(bookRepo.findByAuthor(author));
        return all;
    }

    public Book update(UUID id, String title, String author, String isbn, Integer quantity){
        Optional<Book> book = bookRepo.findById(id);
        book.ifPresent(book1 -> {
            if (title != null) book1.setTitle(title);
            if (author != null) book1.setAuthor(author);
            if (isbn != null) book1.setIsbn(isbn);
            if (quantity != null) book1.setQuantity(quantity);
        });
        return bookRepo.save(book.orElseThrow());
    }
}
