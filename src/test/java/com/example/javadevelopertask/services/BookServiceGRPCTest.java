package com.example.javadevelopertask.services;

import books.*;
import com.example.javadevelopertask.model.entity.Book;
import com.example.javadevelopertask.repo.BookRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

class BookServiceGRPCTest {
    private BookServiceGrpc.BookServiceBlockingStub bookServiceBlockingStub;
    private BookRepo bookRepo;
    private BookServiceGRPC bookServiceGRPC;

    @BeforeEach
    public void setUp() {
        bookServiceBlockingStub = mock(BookServiceGrpc.BookServiceBlockingStub.class);
        bookRepo = mock(BookRepo.class);
        bookServiceGRPC = new BookServiceGRPC(bookRepo);
    }

    @Test
    void testCreateBook() {
        CreateBookRequest request = CreateBookRequest.newBuilder()
                .setAuthor("Author")
                .setTitle("Title")
                .setIsbn("ISBN")
                .setQuantity(10)
                .build();

        Book savedBook = Book.builder()
                .id(UUID.randomUUID())
                .author(request.getAuthor())
                .title(request.getTitle())
                .isbn(request.getIsbn())
                .quantity(request.getQuantity())
                .date(new Date())
                .build();

        when(bookRepo.save(any())).thenReturn(savedBook);

        StreamObserver<CreateBookResponse> responseObserver = mock(StreamObserver.class);

        bookServiceGRPC.createBook(request, responseObserver);

        verify(responseObserver).onNext(any());
        verify(responseObserver).onCompleted();
    }

    @Test
    void testReadBook() {
        UUID id = UUID.randomUUID();

        ReadBookRequest request = ReadBookRequest.newBuilder()
                .setId(id.toString())
                .build();

        Book book = Book.builder()
                .id(id)
                .author("Author")
                .title("Title")
                .isbn("ISBN")
                .quantity(10)
                .date(new Date())
                .build();

        when(bookRepo.findById(id)).thenReturn(Optional.of(book));

        StreamObserver<ReadBookResponse> responseObserver = mock(StreamObserver.class);

        bookServiceGRPC.readBook(request, responseObserver);

        verify(responseObserver).onNext(any());
        verify(responseObserver).onCompleted();
    }

    @Test
    void testDeleteBook() {
        UUID id = UUID.randomUUID();

        DeleteBookRequest request = DeleteBookRequest.newBuilder()
                .setId(id.toString())
                .build();

        when(bookRepo.findById(id)).thenReturn(Optional.of(new Book()));

        StreamObserver<DeleteBookResponse> responseObserver = mock(StreamObserver.class);

        bookServiceGRPC.deleteBook(request, responseObserver);

        verify(responseObserver).onNext(any());
        verify(responseObserver).onCompleted();
    }

    @Test
    void testUpdateBook() {
        UUID id = UUID.randomUUID();

        UpdateBookRequest request = UpdateBookRequest.newBuilder()
                .setId(id.toString())
                .setAuthor("Updated Author")
                .setTitle("Updated Title")
                .setIsbn("Updated ISBN")
                .setQuantity(20)
                .build();

        Book existingBook = Book.builder()
                .id(id)
                .author("Author")
                .title("Title")
                .isbn("ISBN")
                .quantity(10)
                .date(new Date())
                .build();

        when(bookRepo.findById(id)).thenReturn(Optional.of(existingBook));
        when(bookRepo.save(any())).thenReturn(existingBook);

        StreamObserver<UpdateBookResponse> responseObserver = mock(StreamObserver.class);

        bookServiceGRPC.updateBook(request, responseObserver);

        verify(responseObserver).onNext(any());
        verify(responseObserver).onCompleted();
    }
}