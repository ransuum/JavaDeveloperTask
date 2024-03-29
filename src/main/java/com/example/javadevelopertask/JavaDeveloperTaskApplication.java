package com.example.javadevelopertask;

import com.example.javadevelopertask.services.BookService;
import com.example.javadevelopertask.services.BookServiceGRPC;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class JavaDeveloperTaskApplication {
    private static final Logger logger = LoggerFactory.getLogger(JavaDeveloperTaskApplication.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(JavaDeveloperTaskApplication.class, args);
        Server server = ServerBuilder.forPort(50051)
                .addService(new BookServiceGRPC())
                .build();
        server.start();
        logger.info("gRPC server started on port " + server.getPort());
        server.awaitTermination();
    }

}
