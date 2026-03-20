// cmd/server/main.go
package main

import (
	"context"
	"log"
	"net/http"

	"demobanking.com/notification/internal/consumer"
	"demobanking.com/notification/internal/delivery"
	"demobanking.com/notification/internal/model"
	"demobanking.com/notification/internal/notifier"
)

func main() {
	broker := notifier.NewBroker()

	consumer := consumer.NewConsumer(
		[]string{"localhost:9092"},
		"TRANSACTION_CREATED_EVENTS_TOPIC",
		"transaction-orchestrator-group-GO",
	)

	go consumer.Start(context.Background(), func(event model.TransactionCreatedEvent) {
		broker.Broadcast(event.TransactionType.String())
	})

	http.HandleFunc("/events", delivery.SSEHandler(broker))

	log.Println("Server running on : 8086")
	log.Fatal(http.ListenAndServe(":8086", nil))
}
