package consumer

import (
	"context"
	"log"

	"demobanking.com/notification/internal/model"
	"github.com/segmentio/kafka-go"
	"google.golang.org/protobuf/proto"
)

type Consumer struct {
	reader *kafka.Reader
}

func NewConsumer(brokers []string, topic string, groupID string) *Consumer {
	return &Consumer{
		reader: kafka.NewReader(
			kafka.ReaderConfig{
				Brokers: brokers,
				Topic:   topic,
				GroupID: groupID}),
	}
}

func (c *Consumer) Start(ctx context.Context, handler func(model.TransactionCreatedEvent)) {

	for {
		msg, err := c.reader.ReadMessage(ctx)
		payload := msg.Value

		if err != nil {
			log.Println("error reading message:", err)
			continue
		}

		var event model.TransactionCreatedEvent

		if err := proto.Unmarshal(payload, &event); err != nil {
			log.Println("invalid event:", err)
			continue
		}
		handler(event)
	}
}
