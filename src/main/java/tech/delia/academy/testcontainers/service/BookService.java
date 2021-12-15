package tech.delia.academy.testcontainers.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import tech.delia.academy.testcontainers.model.Book;
import tech.delia.academy.testcontainers.repository.BookRepository;

@Service
public class BookService {

  private final BookRepository bookRepository;

  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public Book save(Book book) {
    return bookRepository.save(book);
  }

  public Optional<Book> findById(String id) {
    return bookRepository.findById(id);
  }

  public List<Book> findAll() {
    return bookRepository.findAll();
  }

  public void deleteById(String id) {
    bookRepository.deleteById(id);
  }
}
