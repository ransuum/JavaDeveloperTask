package com.example.javadevelopertask.utilDto.dto.mapper;

import books.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapperForGrpc {
    MapperForGrpc INST = Mappers.getMapper(MapperForGrpc.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "quantity", source = "quantity")
    Book toBookReal(com.example.javadevelopertask.model.entity.Book book);
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "quantity", source = "quantity")
    com.example.javadevelopertask.model.entity.Book realBookToBook(Book book);

}
