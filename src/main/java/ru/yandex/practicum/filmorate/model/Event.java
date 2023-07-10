package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private int eventId;
    private int userId;
    private int entityId;
    private long timestamp;
    private EventType eventType;
    private Operation operation;

    public Event(int userId, int entityId, EventType eventType, Operation operation) {
        this.userId = userId;
        this.entityId = entityId;
        this.eventType = eventType;
        this.operation = operation;
    }

    public enum EventType {
        LIKE,
        REVIEW,
        FRIEND
    }

    public enum Operation {
        REMOVE,
        ADD,
        UPDATE
    }
}
