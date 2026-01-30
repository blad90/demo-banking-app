package com.demobanking.events;

public record UserValidatedEvent(Long userId, boolean validated) { }