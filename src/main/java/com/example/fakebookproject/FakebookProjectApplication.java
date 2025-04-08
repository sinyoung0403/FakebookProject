package com.example.fakebookproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FakebookProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(FakebookProjectApplication.class, args);
  }

}
