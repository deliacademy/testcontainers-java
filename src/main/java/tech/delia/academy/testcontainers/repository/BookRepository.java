package tech.delia.academy.testcontainers.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import tech.delia.academy.testcontainers.model.Book;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
}
