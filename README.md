# DemoBank
#### by BLAD

## Description
Event-driven banking application, for demo purposes using the Saga pattern, 
Spring Boot microservices, Kafka, OpenTelemetry tracing, Docker and Terraform.

## Architecture
![architecture.png](docs/architecture/architecture.png)

## Clone the repository
1. Using the terminal, go to the folder or directory for the project.
2. In the terminal, include the following command: \
   `git clone https://github.com/blad90/demo-banking-app.git`
3. Inside project root folder, in order to set up and build the containers, run the below command: \
   `docker-compose up -d`

## Endpoints
- API Gateway: `/8080`
- Users: `/8081/users`
- Accounts: `/8082/accounts`
- Transactions: `/8083/transactions`

## Extras
### For rebuilding docker containers
- `docker compose down`
- `docker compose build --no-cache [NAME]-service`
- `docker compose up [NAME]-service`
