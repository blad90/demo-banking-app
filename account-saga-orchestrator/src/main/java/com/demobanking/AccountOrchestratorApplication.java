package com.demobanking;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@OpenAPIDefinition(
        info = @Info(
                title = "Account Orchestrator API",
                version = "v1",
                description = "Account Orchestrator Endpoints"
        )
)
@SpringBootApplication
public class AccountOrchestratorApplication {
    static void main(String[] args) {
        SpringApplication.run(AccountOrchestratorApplication.class, args);
    }
}
