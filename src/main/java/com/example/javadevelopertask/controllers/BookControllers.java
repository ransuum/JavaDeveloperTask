package com.example.javadevelopertask.controllers;

import com.example.javadevelopertask.model.dto.BookDto;
import com.example.javadevelopertask.services.BookService;
import com.example.javadevelopertask.utilDto.dto.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
public class BookControllers {
    private BookService bookService;
    private BookMapper bookMapper;
    @Autowired
    public BookControllers(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }
    @PostMapping("/create")
    @PreAuthorize("@bookAccessService.checkRole(authentication.principal.id, T(com.example.javadevelopertask.enums.UserRoles).ADMIN)")
    public ResponseEntity<BookDto> create(@RequestParam String title, @RequestParam String author, @RequestParam String isbn, @RequestParam Integer quantity){
            return new ResponseEntity<>(bookMapper.bookToDto(bookService
                    .create(title, author, isbn, quantity)), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    @PreAuthorize("@bookAccessService.checkRole(authentication.principal.id, T(com.example.javadevelopertask.enums.UserRoles).ADMIN)")
    public ResponseEntity<BookDto> update(@RequestParam UUID id, @RequestParam(required = false) String title, @RequestParam(required = false) String author,
                                          @RequestParam(required = false) String isbn, @RequestParam(required = false) Integer quantity){
        return new ResponseEntity<>(bookMapper.bookToDto(bookService
                .update(id, title, author, isbn, quantity)), HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity<BookDto> findById(@RequestParam UUID id){
        return new ResponseEntity<>(bookMapper.bookToDto(bookService
                .findById(id).orElseThrow()), HttpStatus.OK);
    }
    @DeleteMapping("/id")
    @PreAuthorize("@bookAccessService.checkRole(authentication.principal.id, T(com.example.javadevelopertask.enums.UserRoles).ADMIN)")
    public ResponseEntity<UUID> deleteById(@RequestParam UUID id){
        return new ResponseEntity<>(bookService.deleteById(id), HttpStatus.OK);
    }
    @DeleteMapping("/author")
    @PreAuthorize("@bookAccessService.checkRole(authentication.principal.id, T(com.example.javadevelopertask.enums.UserRoles).ADMIN)")
    public ResponseEntity<List<BookDto>> findAuthorsAndDelete(String author){
        return new ResponseEntity<>(bookService.findAuthorsAndDelete(author).stream()
                .map(bookMapper::bookToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/before")
    public ResponseEntity<List<BookDto>> findByDateBefore(@RequestParam String date) throws ParseException {
        return new ResponseEntity<>(bookService.findBeforeDate(date).stream().map(bookMapper::bookToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }
    @GetMapping("/after")
    public ResponseEntity<List<BookDto>> findByDateAfter(@RequestParam String date) throws ParseException {
        return new ResponseEntity<>(bookService.findAfterDate(date).stream()
                .map(bookMapper::bookToDto).collect(Collectors.toList()), HttpStatus.OK);
    }
}
