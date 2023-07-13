package ru.yandex.practicum.filmorate.storage.user.feeds;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;
import java.util.Optional;

public interface FeedStorage {
    Optional<Event> findById(int id);

    List<Event> getAll();

    Event addEvent(Event event);

    Event update(Event event);

    List<Event> findEventsByUserId(int id);
}
