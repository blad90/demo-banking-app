# DemoBank — Claude Code Development Guidelines

## Project Overview

DemoBank is a demo event-driven banking application designed to demonstrate modern backend and distributed-systems engineering practices.

The project is NOT intended for real financial transactions.

Primary architectural concepts:

* Java / Spring Boot microservices
* Event-driven architecture
* Saga pattern
* Saga orchestration
* Apache Kafka
* REST APIs
* PostgreSQL
* OpenTelemetry distributed tracing
* Docker / Docker Compose
* Kubernetes / Minikube
* Terraform
* CI/CD with GitHub Actions
* TypeScript-based UI

---

## Repository Structure

The repository contains:

* `services/` — backend microservices
* `account-saga-orchestrator/` — account-related Saga orchestration
* `transaction-saga-orchestrator/` — transaction-related Saga orchestration
* `serialization-schemas/` — shared event/message serialization schemas
* `infrastructure/` — Docker/Kubernetes/infrastructure configuration
* `ui/demo-banking-ui/` — TypeScript UI
* `.github/workflows/` — CI/CD workflows
* `docker-compose.yml` — local container orchestration
* `pom.xml` — Maven project configuration

Do not assume this is an Angular application.

---

## Architectural Principles

Preserve the existing microservices architecture.

Do not convert the project into a monolith.

Do not introduce a new architectural style unless explicitly requested.

Maintain clear service boundaries.

Avoid coupling microservices through direct database access.

Services should communicate through their established REST and/or Kafka mechanisms.

Respect the existing Saga orchestration approach.

Do not replace orchestration with choreography without explicit approval.

---

## Backend

Primary technology:

* Java
* Spring Boot
* Maven
* Spring ecosystem
* REST
* Kafka
* PostgreSQL

Follow existing package structures and coding conventions.

Prefer the existing project patterns over introducing new patterns.

Do not introduce new frameworks or dependencies without explaining why they are necessary.

---

## Event-Driven Architecture

Kafka is a core component of the system.

When modifying event-driven functionality:

1. Identify the producer.
2. Identify the Kafka topic/event.
3. Identify the consumer(s).
4. Identify the serialization schema.
5. Identify the Saga/orchestration impact.
6. Consider retry behavior.
7. Consider failure handling.
8. Consider idempotency.
9. Consider transaction boundaries.
10. Consider observability/tracing.

Do not modify event contracts casually.

Changes to shared serialization schemas may affect multiple services.

Before changing an event contract, identify all producers and consumers.

---

## Saga Pattern

The project uses Saga orchestration.

When modifying a Saga:

* Identify the Saga state.
* Identify commands/events.
* Identify participating services.
* Identify successful execution paths.
* Identify failure paths.
* Identify compensating actions.
* Consider partial failures.
* Consider duplicate messages.
* Consider retries.
* Consider idempotency.

Never implement only the happy path for financial transaction workflows.

---

## Banking Domain

Treat monetary operations as correctness-critical.

Use appropriate monetary representations.

Do not use floating-point arithmetic for financial amounts unless the existing implementation explicitly requires it.

Preserve transaction consistency.

Consider:

* insufficient funds
* duplicate transactions
* concurrent operations
* failed downstream services
* retries
* compensation
* transaction status
* eventual consistency

Never assume that a successful HTTP response alone means that a distributed transaction has completed successfully.

---

## Database

PostgreSQL is used by the application.

Respect service ownership of data.

Do not create cross-service database dependencies.

Before modifying database structures:

1. Identify the owning service.
2. Identify affected entities/tables.
3. Identify migrations or initialization scripts.
4. Identify application code affected.
5. Identify integration-test implications.

---

## Observability

OpenTelemetry is part of the architecture.

When adding or modifying distributed operations, preserve traceability across service boundaries where applicable.

Consider:

* trace IDs
* span propagation
* Kafka message tracing
* REST calls
* Saga execution
* error visibility

Do not remove existing instrumentation simply to simplify implementation.

---

## Docker / Infrastructure

The project uses Docker and Docker Compose.

Before changing container configuration:

* Check `docker-compose.yml`
* Check service Dockerfiles
* Check environment variables
* Check networking
* Check service dependencies
* Check health checks
* Check database configuration
* Check Kafka configuration

Do not change infrastructure configuration unnecessarily.

---

## Kubernetes

The project contains Kubernetes configuration under `infrastructure/`.

Do not assume that Docker Compose configuration and Kubernetes configuration are interchangeable.

When changing deployment behavior, identify whether both environments need to be updated.

---

## UI

The UI is TypeScript-based, based on Next.js.

Do not assume Angular unless the existing UI implementation explicitly uses Angular.

Inspect the actual UI framework and existing conventions before modifying it.

Preserve the existing UI architecture.

---

## Testing

Every new feature should include appropriate tests.

Consider:

* unit tests
* integration tests
* REST API tests
* Kafka/event tests
* Saga tests
* failure/compensation tests
* database tests

For distributed workflows, prioritize testing failure scenarios rather than only happy paths.

---

## Security

Treat authentication, authorization, credentials, secrets, and financial operations as security-sensitive.

Never hardcode:

* passwords
* API keys
* tokens
* private keys
* database credentials

Do not expose secrets in logs.

Do not weaken security controls simply to make tests or local development easier.

---

## Git

Do not commit changes automatically unless explicitly requested.

Keep changes focused.

Do not modify unrelated files.

Before making significant changes:

1. Inspect the current implementation.
2. Explain the proposed approach.
3. Identify affected services/components.
4. Identify risks.
5. Implement only after approval when the change is architectural or potentially disruptive.

After implementation:

1. Run relevant tests.
2. Review the changed files.
3. Report failures.
4. Explain important design decisions.

---

## Development Workflow

For significant features, follow:

Requirement
→ Repository analysis
→ Architecture impact analysis
→ Implementation plan
→ Approval
→ Implementation
→ Tests
→ Failure analysis
→ Refactoring
→ Final review

Do not immediately implement large requests without first understanding the existing architecture.

---

## Important Rule

The existing architecture is intentional.

Before introducing a new solution, search the repository for an existing implementation or established pattern.

Prefer:

"understand → extend → test"

over:

"rewrite → replace → simplify"

Do not rewrite working components merely because a different implementation appears cleaner.

---

## AI-Assisted Development

Claude Code is being used as an engineering assistant.

The developer remains responsible for:

* architecture
* requirements
* security decisions
* business rules
* code review
* testing
* dependency decisions
* infrastructure decisions

Claude Code should not make large architectural changes without explicit approval.

When uncertain about an architectural or business decision, stop and explain the uncertainty rather than guessing.
