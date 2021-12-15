package tech.delia.academy.testcontainers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class TestcontainersDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(TestcontainersDemoApplication.class, args);
  }
}
