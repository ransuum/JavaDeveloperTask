package com.example.javadevelopertask.services;


import books.*;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.endpoint.Endpoint;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.http.client.HttpClientBuilder;
import com.consol.citrus.message.Message;
import com.consol.citrus.validation.ValidationProcessor;
import io.grpc.netty.shaded.io.grpc.netty.GrpcHttp2ConnectionHandler;
import org.junit.jupiter.api.Test;
import org.lognet.springboot.grpc.GRpcServerBuilderConfigurer;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.BeforeClass;

import java.util.UUID;

import static com.consol.citrus.container.HamcrestConditionExpression.assertThat;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static org.hamcrest.Matchers.containsString;


public class BookServiceGRPCTest1{
    @CitrusResource
    private HttpClient httpClient;

    @BeforeClass
    public void setUp() {
        this.httpClient = new HttpClientBuilder()
                .requestUrl("http://localhost:50051")  // Замените на URL вашего GRPC сервиса
                .build();
    }
    @Test
    @CitrusTest
    void createBook() {
        CreateBookRequest createBookRequest = CreateBookRequest.newBuilder()
                .setAuthor("Author Name")
                .setTitle("Book Title")
                .setIsbn("ISBN Number")
                .setQuantity(1)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(createBookRequest.getAuthor());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert CreateBookRequest to JSON", e);
        }
        GRpcServerBuilderConfigurer serverConfigurer = new GRpcServerBuilderConfigurer();
        io.grpc.ServerBuilder<?> serverBuilder = io.grpc.ServerBuilder.forPort(50051);
        http().server((Endpoint) serverBuilder)
                .receive()
                .post("/createBook")
                .message().contentType("application/json")
                .body(requestBody);
        http().server((Endpoint) serverBuilder)  // Замените на URL вашего GRPC сервиса
                .send()
                .response(HttpStatus.CREATED)
                .message().contentType("application/json")
                .header("Content-Type", "application/json");
    }
    @Test
    @CitrusTest
    void readBook() {
        ReadBookRequest readBookRequest = ReadBookRequest.newBuilder()
                .setId(String.valueOf(UUID.randomUUID()))
                .build();
        http().client(httpClient)
                .send()
                .post("BookService/readBook")
                .message().contentType("application/json")
                .body(readBookRequest.getId());
        http().client(httpClient)
                .receive()
                .response(HttpStatus.OK)
                .message().contentType("application/json")
                .validate()
                .build();
    }

    @Test
    @CitrusTest
    void deleteBook() {
        DeleteBookRequest deleteBookRequest = DeleteBookRequest.newBuilder()
                .setId(String.valueOf(UUID.randomUUID()))
                .build();
        http().client(httpClient)
                .send()
                .post("BookService/deleteBook")
                .message().contentType("application/json")
                .body(deleteBookRequest.getId());
        http().client(httpClient)
                .receive()
                .response(HttpStatus.OK)
                .message().contentType("application/json")
                .validate()
                .build();
    }

    @Test
    void updateBook() {
        UpdateBookRequest updateBookRequest = UpdateBookRequest.newBuilder()
                .setId(String.valueOf(UUID.randomUUID()))
                .setAuthor("Elon")
                .setTitle("AI")
                .setIsbn("43985603-3634643-346346")
                .setQuantity(4)
                .build();
        http().client(httpClient)
                .send()
                .put("BookService/updateBook")
                .message().contentType("application/json")
                .body(updateBookRequest.getId())
                .body(updateBookRequest.getAuthor())
                .body(updateBookRequest.getTitle())
                .body(updateBookRequest.getIsbn())
                .body(String.valueOf(updateBookRequest.getQuantity()));
        http().client(httpClient)
                .receive()
                .response(HttpStatus.OK)
                .message().contentType("application/json")
                .validate()
                .build();
    }
}