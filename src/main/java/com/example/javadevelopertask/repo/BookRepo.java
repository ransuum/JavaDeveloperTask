package com.example.javadevelopertask.repo;

import com.example.javadevelopertask.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepo extends JpaRepository<Book, UUID> {
    List<Book> findByAuthor(String author);
}
