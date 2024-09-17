package me.ikno.documentapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class DocumentApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocumentApiApplication.class, args);
    }

}
