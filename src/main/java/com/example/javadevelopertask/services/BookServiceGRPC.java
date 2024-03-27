package com.example.javadevelopertask.services;

import books.*;
import com.example.javadevelopertask.model.entity.Book;
import com.example.javadevelopertask.repo.BookRepo;
import com.example.javadevelopertask.utilDto.dto.mapper.MapperForGrpc;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@GRpcService
public class BookServiceGRPC extends BookServiceGrpc.BookServiceImplBase {
    @Autowired
    private BookRepo bookRepo;

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
                .build();
        createBookResponseStreamObserver.onNext(createBookResponse);
        createBookResponseStreamObserver.onCompleted();
    }

    @Override
    public void readBook(ReadBookRequest request, StreamObserver<ReadBookResponse> responseObserver) {
        Optional<Book> optionalBook = bookRepo.findById(UUID.fromString(request.getId()));
        if (optionalBook.isPresent()) {
            books.Book book = MapperForGrpc.INST.toBookReal(optionalBook.get());
            ReadBookResponse response = ReadBookResponse.newBuilder()
                    .setBook(book)
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
        byId.ifPresent(book -> {
            book.setAuthor(request.getAuthor());
            book.setIsbn(request.getIsbn());
            book.setTitle(request.getTitle());
            book.setQuantity(request.getQuantity());
            bookRepo.save(book);

            UpdateBookResponse updateBookResponse = UpdateBookResponse.newBuilder()
                    .setBook(MapperForGrpc.INST.toBookReal(book))
                    .build();

            updateBookResponseStreamObserver.onNext(updateBookResponse);
            updateBookResponseStreamObserver.onCompleted();
        });
    }
}
