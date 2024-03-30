package com.example.javadevelopertask.server;

import com.example.javadevelopertask.repo.BookRepo;
import com.example.javadevelopertask.services.BookServiceGRPC;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ServerGrpc {
    BookRepo bookRepo;
    private static final Logger logger = LoggerFactory.getLogger(ServerGrpc.class);

    public ServerGrpc(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @PostConstruct
    public void startServer() {
        new Thread(() -> {
            try {
                Server server = ServerBuilder.forPort(50051)
                        .addService(new BookServiceGRPC(bookRepo))
                        .build();
                server.start();
                logger.info("gRPC server started on port " + server.getPort());
                server.awaitTermination();
            } catch (IOException | InterruptedException e) {
                logger.error("Error starting gRPC server", e);
            }
        }).start();
    }
}
