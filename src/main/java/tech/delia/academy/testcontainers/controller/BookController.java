package tech.delia.academy.testcontainers.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import tech.delia.academy.testcontainers.model.Book;
import tech.delia.academy.testcontainers.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {
  private static final Logger LOG = LoggerFactory.getLogger(BookController.class);

  private final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @PostMapping
  public Book save(@RequestBody Book book) {
    LOG.info("Saving book {}", book);

    return bookService.save(book);
  }

  @GetMapping()
  public List<Book> findAll() {
    LOG.info("Retrieving all books");

    return bookService.findAll();
  }

  @GetMapping("/{id}")
  public Book findById(@PathVariable String id) {
    LOG.info("Retrieving book with id={}", id);

    return bookService.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteById(@PathVariable String id) {
    LOG.info("Deleting book with id={}", id);

    bookService.deleteById(id);

    return ResponseEntity.ok().build();
  }
}
