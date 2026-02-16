package com.banking.transaction;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@OpenAPIDefinition(
        info = @Info(
                title = "Transaction Service API",
                version = "v1",
                description = "Transaction Endpoints"
        )
        //, servers = @Server(url = "http://192.168.49.2:30080/app1", description = "API Gateway")
)
@SpringBootApplication
public class TransactionServiceApplication {
    static void main(String[] args) {
        SpringApplication.run(TransactionServiceApplication.class, args);
    }
}
