package com.blogsphere.blogsphere.event;

import java.time.Instant;
import java.util.UUID;

public class EventEnvelope<T> {

    private String eventId;
    private String eventType;
    private Instant timestamp;
    private T payload;

    public EventEnvelope(String eventType, T payload) {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.timestamp = Instant.now();
        this.payload = payload;
    }

    public String getEventId() { return eventId; }
    public String getEventType() { return eventType; }
    public Instant getTimestamp() { return timestamp; }
    public T getPayload() { return payload; }
}