package tech.delia.academy.testcontainers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.*;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import tech.delia.academy.testcontainers.model.Author;
import tech.delia.academy.testcontainers.model.Book;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = TestcontainersIT.MongoDbInitializer.class)
public class TestcontainersIT {
    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4")
        // https://github.com/testcontainers/testcontainers-java/issues/3077
        .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));

    public static class MongoDbInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
          TestPropertyValues.of(
                  "spring.data.mongodb.uri=" + mongoDBContainer.getReplicaSetUrl()
              )
              .applyTo(configurableApplicationContext);
        }
    }

  @Autowired
  protected WebApplicationContext webApplicationContext;

  @BeforeEach
  void initialiseRestAssuredMockMvcWebApplicationContext() {
    RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
  }

  @Test
  void insertBooksRetrieveAllAndDeleteOne() {
    // book Dune
    Book book1 = new Book();
    book1.title = "Dune";
    Author author1 = book1.author = new Author();
    author1.firstname = "Frank";
    author1.lastname = "Herbert";

    // book Foundation
    Book book2 = new Book();
    book2.title = "Foundation";
    Author author2 = book2.author = new Author();
    author2.firstname = "Isaac";
    author2.lastname = "Asimov";

    // Save books
    List.of(book1, book2).forEach(book -> {
      given()
          .contentType(ContentType.JSON)
          .body(book)
      .when()
          .post("/books")
      .then()
          .log().ifValidationFails()
          .status(HttpStatus.OK);
    });

    // Get All
    List<Book> books = given()
    .when()
        .get("/books")
    .then()
        .log().ifValidationFails()
        .status(HttpStatus.OK)
        .body("$", hasSize(2))
        .body("title", contains("Dune", "Foundation"))
        .body("author.lastname", contains("Herbert", "Asimov"))
        .extract().body().jsonPath().getList(".", Book.class);

    // Get Dune
    given()
    .when()
        .get("/books/{id}", books.get(0).id)
    .then()
        .log().ifValidationFails()
        .status(HttpStatus.OK)
        .body("title", is("Dune"))
        .body("author.lastname", is("Herbert"));

    // Delete Dune
    given()
    .when()
        .delete("/books/{id}", books.get(0).id)
    .then()
        .log().ifValidationFails()
        .status(HttpStatus.OK);

    // Get Dune -> 404
    given()
    .when()
        .get("/books/{id}", books.get(0).id)
    .then()
        .log().ifValidationFails()
        .status(HttpStatus.NOT_FOUND);

    // Get All, only Foundation left
    given()
    .when()
        .get("/books")
    .then()
        .log().ifValidationFails()
        .status(HttpStatus.OK)
        .body("$", hasSize(1))
        .body("title", contains("Foundation"))
        .body("author.lastname", contains("Asimov"));
  }
}
