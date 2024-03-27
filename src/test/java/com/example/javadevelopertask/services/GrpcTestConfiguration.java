package com.example.javadevelopertask.services;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class GrpcTestConfiguration {
    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>()
            .withDatabaseName("book")
            .withUsername("postgres")
            .withPassword("82232");
}
