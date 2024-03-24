package com.example.javadevelopertask.services;

import com.example.javadevelopertask.model.entity.Book;
import com.example.javadevelopertask.repo.BookRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class BookServiceTest {
    @Mock
    private BookRepo bookRepo;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() {
        String title = "Test Title";
        String author = "Test Author";
        String isbn = "1234567890";
        Integer quantity = 10;

        Book mockBook = new Book(UUID.randomUUID(), title, author, isbn, quantity);
        when(bookRepo.save(any(Book.class))).thenReturn(mockBook);

        Book createdBook = bookService.create(title, author, isbn, quantity);

        assertNotNull(createdBook);
        assertEquals(title, createdBook.getTitle());
        assertEquals(author, createdBook.getAuthor());
        assertEquals(isbn, createdBook.getIsbn());
        assertEquals(quantity, createdBook.getQuantity());
    }

    @Test
    void findById() {
        UUID id = UUID.randomUUID();
        Book mockBook = new Book(id, "Test Title", "Test Author", "1234567890", 10);
        when(bookRepo.findById(id)).thenReturn(Optional.of(mockBook));

        Optional<Book> foundBook = bookService.findById(id);

        assertTrue(foundBook.isPresent());
        assertEquals(mockBook, foundBook.get());
    }

    @Test
    void deleteById() {
        UUID id = UUID.randomUUID();

        UUID deletedId = bookService.deleteById(id);

        assertEquals(id, deletedId);
        verify(bookRepo, times(1)).deleteById(id);
    }

    @Test
    void findAuthorsAndDelete() {
        String author = "Test Author";
        List<Book> mockBooks = Arrays.asList(
                new Book(UUID.randomUUID(), "Title1", author, "1234567890", 10),
                new Book(UUID.randomUUID(), "Title2", author, "0987654321", 5)
        );
        when(bookRepo.findAll()).thenReturn(mockBooks);
        when(bookRepo.findByAuthor(author)).thenReturn(mockBooks);

        List<Book> deletedBooks = bookService.findAuthorsAndDelete(author);

        assertEquals(mockBooks, deletedBooks);
        verify(bookRepo, times(1)).deleteAll(mockBooks);
    }

    @Test
    void update() {
        UUID id = UUID.randomUUID();
        String updatedTitle = "Updated Title";
        String updatedAuthor = "Updated Author";
        String updatedIsbn = "9876543210";
        Integer updatedQuantity = 15;

        Book originalBook = new Book(id, "Original Title", "Original Author", "1234567890", 10);
        when(bookRepo.findById(id)).thenReturn(Optional.of(originalBook));
        when(bookRepo.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book updatedBook = bookService.update(id, updatedTitle, updatedAuthor, updatedIsbn, updatedQuantity);

        assertNotNull(updatedBook);
        assertEquals(updatedTitle, updatedBook.getTitle());
        assertEquals(updatedAuthor, updatedBook.getAuthor());
        assertEquals(updatedIsbn, updatedBook.getIsbn());
        assertEquals(updatedQuantity, updatedBook.getQuantity());
    }
}