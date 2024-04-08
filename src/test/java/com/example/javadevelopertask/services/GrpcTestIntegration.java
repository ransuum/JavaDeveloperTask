package com.example.javadevelopertask.services;

import books.BookServiceGrpc;
import books.CreateBookRequest;
import books.CreateBookResponse;
import com.consol.citrus.annotations.CitrusTest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
public class GrpcTestIntegration {
    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>()
            .withDatabaseName("book")
            .withUsername("postgres")
            .withPassword("82232");

    private ManagedChannel channel;
    private BookServiceGrpc.BookServiceBlockingStub blockingStub;

    @BeforeEach
    public void setUp() {
        channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();

        blockingStub = BookServiceGrpc.newBlockingStub(channel);
    }

    @AfterEach
    public void tearDown() {
        channel.shutdown();
    }

    @Test
    @CitrusTest
    public void testCreateBook() {
        CreateBookRequest request = CreateBookRequest.newBuilder()
                .setAuthor("Test Author")
                .setTitle("Test Title")
                .setIsbn("Test ISBN")
                .setQuantity(5)
                .build();

        CreateBookResponse response = blockingStub.createBook(request);
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("AI", response.getTitle());
        assertEquals("Elon", response.getAuthor());
        assertEquals("25252-3434-14141", response.getIsbn());
        assertEquals(5, response.getQuantity());
    }
}
