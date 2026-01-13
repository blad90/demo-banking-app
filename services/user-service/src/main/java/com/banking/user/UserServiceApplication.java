package com.banking.user;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@OpenAPIDefinition(
        info = @Info(
                title = "User Service API",
                version = "v1",
                description = "User Management Endpoints"
        )
)
@SpringBootApplication
public class UserServiceApplication {
    static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
