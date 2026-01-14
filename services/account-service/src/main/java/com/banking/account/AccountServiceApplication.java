package com.banking.account;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@OpenAPIDefinition(
        info = @Info(
                title = "Account Service API",
                version = "v1",
                description = "Account Management Endpoints"
        )
)
@SpringBootApplication
public class AccountServiceApplication {
    static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }
}
