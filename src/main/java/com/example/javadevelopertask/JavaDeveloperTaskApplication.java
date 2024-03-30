package com.example.javadevelopertask;

import com.example.javadevelopertask.repo.BookRepo;
import com.example.javadevelopertask.server.ServerGrpc;
import com.example.javadevelopertask.services.BookServiceGRPC;
import com.example.javadevelopertask.utilDto.dto.mapper.BookMapper;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class JavaDeveloperTaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(JavaDeveloperTaskApplication.class, args);
    }

}
