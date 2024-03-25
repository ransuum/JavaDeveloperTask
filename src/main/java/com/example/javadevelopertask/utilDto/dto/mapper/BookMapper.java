package com.example.javadevelopertask.utilDto.dto.mapper;

import com.example.javadevelopertask.model.dto.BookDto;
import com.example.javadevelopertask.model.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    public BookDto bookToDto(Book book){
        return BookDto.builder()
                .id(book.getId())
                .date(book.getDate())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .quantity(book.getQuantity())
                .title(book.getTitle())
                .build();
    }
}
