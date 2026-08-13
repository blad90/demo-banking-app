# ---------- BUILD STAGE ----------
FROM golang:1.25-alpine AS build
WORKDIR /app

COPY services/notification-service/go.mod services/notification-service/go.sum ./
RUN go mod download

COPY services/notification-service/. .
RUN CGO_ENABLED=0 go build -o notification-service ./cmd/main.go

# ---------- RUNTIME STAGE ----------
FROM alpine:3.20
WORKDIR /app

COPY --from=build /app/notification-service .

EXPOSE 8086
ENTRYPOINT ["./notification-service"]
