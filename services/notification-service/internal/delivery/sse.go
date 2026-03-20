package delivery

import (
	"fmt"
	"net/http"

	"demobanking.com/notification/internal/notifier"
)

func SSEHandler(broker *notifier.Broker) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		flusher, ok := w.(http.Flusher)
		if !ok {
			http.Error(w, "Streaming unsupported", http.StatusInternalServerError)
			return
		}

		w.Header().Set("Content-Type", "text/event-stream")
		w.Header().Set("Cache-Control", "no-cache")
		w.Header().Set("Connection", "keep-alive")

		ch := broker.Subscribe()
		defer broker.Unsubscribe(ch)

		for msg := range ch {
			fmt.Fprintf(w, "data: %s\n\n", msg)
			flusher.Flush()
		}
	}
}
