package com.example.javadevelopertask.services;

import books.*;
import com.example.javadevelopertask.model.entity.Book;
import com.example.javadevelopertask.repo.BookRepo;
import com.example.javadevelopertask.utilDto.dto.mapper.BookMapper;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@GRpcService
public class BookServiceGRPC extends BookServiceGrpc.BookServiceImplBase {
    BookRepo bookRepo;
    public BookServiceGRPC(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }
    @Override
    public void createBook(CreateBookRequest createBookRequest, StreamObserver<CreateBookResponse> createBookResponseStreamObserver){
        Book book = Book.builder()
                .author(createBookRequest.getAuthor())
                .title(createBookRequest.getTitle())
                .isbn(createBookRequest.getIsbn())
                .date(new Date())
                .quantity(createBookRequest.getQuantity())
                .build();
        Book save = bookRepo.save(book);
        CreateBookResponse createBookResponse = CreateBookResponse.newBuilder()
                .setId(String.valueOf(save.getId()))
                .setTitle(save.getTitle())
                .setAuthor(save.getAuthor())
                .setIsbn(save.getIsbn())
                .setQuantity(save.getQuantity())
                .build();
        createBookResponseStreamObserver.onNext(createBookResponse);
        createBookResponseStreamObserver.onCompleted();
    }

    @Override
    public void readBook(ReadBookRequest request, StreamObserver<ReadBookResponse> responseObserver) {
        Optional<Book> optionalBook = bookRepo.findById(UUID.fromString(request.getId()));
        BookMapper bookMapper = new BookMapper();
        if (optionalBook.isPresent()) {
            ReadBookResponse response = ReadBookResponse.newBuilder()
                    .setBookProto(bookMapper.toBookProto(optionalBook.orElseThrow()))
                    .build();
            responseObserver.onNext(response);
        } else {
            responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void deleteBook(DeleteBookRequest request, StreamObserver<DeleteBookResponse> deleteBookResponseStreamObserver) {
        Optional<Book> byId = bookRepo.findById(UUID.fromString(request.getId()));
        if (byId.isPresent()){
            bookRepo.deleteById(UUID.fromString(request.getId()));
            DeleteBookResponse deleteBookResponse = DeleteBookResponse.newBuilder()
                    .setId(request.getId())
                    .build();
            deleteBookResponseStreamObserver.onNext(deleteBookResponse);
            deleteBookResponseStreamObserver.onCompleted();
        } else {
            deleteBookResponseStreamObserver.onError( new StatusRuntimeException(Status.NOT_FOUND));
        }
    }

    @Override
    public void updateBook(UpdateBookRequest request, StreamObserver<UpdateBookResponse> updateBookResponseStreamObserver){
        Optional<Book> byId = bookRepo.findById(UUID.fromString(request.getId()));
        BookMapper bookMapper = new BookMapper();
        byId.ifPresent(book -> {
            if (!request.getAuthor().isEmpty())book.setAuthor(request.getAuthor());
            if (!request.getIsbn().isEmpty())book.setIsbn(request.getIsbn());
            if (!request.getTitle().isEmpty())book.setTitle(request.getTitle());
            book.setQuantity(request.getQuantity());
            bookRepo.save(book);
            UpdateBookResponse updateBookResponse = UpdateBookResponse.newBuilder()
                    .setBookProto(bookMapper.toBookProto(book))
                    .build();
            updateBookResponseStreamObserver.onNext(updateBookResponse);
            updateBookResponseStreamObserver.onCompleted();
        });
    }
}
