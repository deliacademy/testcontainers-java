package tech.delia.academy.testcontainers.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "books")
public class Book {

  public String id;

  public String title;

  public Author author;

}
