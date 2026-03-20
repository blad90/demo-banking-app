package notifier

import "sync"

type Broker struct {
	clients map[chan string]bool
	mu      sync.Mutex
}

func NewBroker() *Broker {
	return &Broker{
		clients: make(map[chan string]bool),
	}
}

func (b *Broker) Subscribe() chan string {
	ch := make(chan string)

	b.mu.Lock()
	b.clients[ch] = true
	b.mu.Unlock()

	return ch
}

func (b *Broker) Unsubscribe(ch chan string) {
	b.mu.Lock()
	delete(b.clients, ch)
	close(ch)
	b.mu.Unlock()
}

func (b *Broker) Broadcast(msg string) {
	b.mu.Lock()
	defer b.mu.Unlock()

	for ch := range b.clients {
		select {
		case ch <- msg:
		default:
		}
	}
}
