package com.example.javadevelopertask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private UUID id;
    private String title;
    private String author;
    private String isbn;
    private Integer quantity;
}
