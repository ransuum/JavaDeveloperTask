package com.example.javadevelopertask.services;

import books.CreateBookRequest;
import books.CreateBookResponse;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.*;

import static com.example.javadevelopertask.services.GrpcTestConfiguration.postgresContainer;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BookServiceGRPCTest {
    @Autowired
    private BookServiceGRPC bookServiceGRPC;

    @Test
    public void testCreateBook() {
        CreateBookRequest request = CreateBookRequest.newBuilder()
                .setAuthor("Author")
                .setTitle("Title")
                .setIsbn("ISBN")
                .setQuantity(10)
                .build();

        TestStreamObserver<CreateBookResponse> responseObserver = new TestStreamObserver<>();

        bookServiceGRPC.createBook(request, responseObserver);

        CreateBookResponse response = responseObserver.getValue();

        assertNotNull(response);
        assertNotNull(response.getId());
    }
    private static class TestStreamObserver<T> implements StreamObserver<T> {
        private T value;
        private Throwable error;

        @Override
        public void onNext(T value) {
            this.value = value;
        }

        @Override
        public void onError(Throwable t) {
            this.error = t;
        }

        @Override
        public void onCompleted() {
            // Do nothing
        }

        public T getValue() {
            return value;
        }

        public Throwable getError() {
            return error;
        }
    }

    @Test
    public void testCreateBook1() throws SQLException {
        TestStreamObserver<CreateBookResponse> responseObserver = new TestStreamObserver<>();
        CreateBookRequest request = CreateBookRequest.newBuilder()
                .setAuthor("Author")
                .setTitle("Title")
                .setIsbn("ISBN")
                .setQuantity(10)
                .build();

        bookServiceGRPC.createBook(request, responseObserver);

        // Проверка данных в базе данных
        try (Connection connection = DriverManager.getConnection(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword())) {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM books WHERE id = ?");
            preparedStatement.setString(1, responseObserver.getValue().getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            assertTrue(resultSet.next());
            assertEquals("Author", resultSet.getString("author"));
            assertEquals("Title", resultSet.getString("title"));
            assertEquals("ISBN", resultSet.getString("isbn"));
            assertEquals(10, resultSet.getInt("quantity"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}