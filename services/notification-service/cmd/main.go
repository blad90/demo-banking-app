// cmd/server/main.go
package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"strings"

	"demobanking.com/notification/internal/auth"
	"demobanking.com/notification/internal/consumer"
	"demobanking.com/notification/internal/delivery"
	"demobanking.com/notification/internal/model"
	"demobanking.com/notification/internal/notifier"
)

func kafkaBrokers() []string {
	if raw := os.Getenv("KAFKA_BROKERS"); raw != "" {
		return strings.Split(raw, ",")
	}
	return []string{"localhost:9092"}
}

func oidcIssuerURL() string {
	if raw := os.Getenv("OIDC_ISSUER_URL"); raw != "" {
		return raw
	}
	return "http://localhost:9090/realms/demo-bank-realm"
}

func main() {
	broker := notifier.NewBroker()

	consumer := consumer.NewConsumer(
		kafkaBrokers(),
		"TRANSACTION_CREATED_EVENTS_TOPIC",
		"transaction-orchestrator-group-GO",
	)

	go consumer.Start(context.Background(), func(event model.TransactionCreatedEvent) {
		descriptionDetail := "Customer ID: " + event.CustomerId + " ; Account No." + event.SourceAccountNumber + " Destination account: " +
			event.DestinationAccountNumber + " Amount: " + event.Amount + " Description: " + event.Description

		broker.Broadcast(descriptionDetail)
	})

	http.HandleFunc("/events", auth.RequireToken(oidcIssuerURL())(delivery.SSEHandler(broker)))

	log.Println("Server running on : 8086")
	log.Fatal(http.ListenAndServe(":8086", nil))
}
