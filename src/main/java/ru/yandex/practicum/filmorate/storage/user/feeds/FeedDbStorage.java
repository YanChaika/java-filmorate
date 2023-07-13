package ru.yandex.practicum.filmorate.storage.user.feeds;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FeedDbStorage implements FeedStorage {

    private final JdbcTemplate jdbcTemplate;
    private final String sqlQueryGet = "SELECT f.event_id, f.timestamp, f.user_id, e.name AS eventType, o.name AS operation, f.entity_id " +
            "FROM PUBLIC.feeds f " +
            "JOIN PUBLIC.event_types e ON f.eventType_id = e.id " +
            "JOIN PUBLIC.operations o ON f.operation_id = o.id";

    @Override
    public Optional<Event> findById(int id) {
        log.info("Получение события по id: {}", id);
        String sqlQuery = sqlQueryGet + " WHERE id=?";
        return jdbcTemplate.query(sqlQuery, new EventMap(), id).stream().findAny();
    }

    @Override
    public List<Event> getAll() {
        log.info("Получение всех событий");
        String sqlQuery = sqlQueryGet;
        return jdbcTemplate.query(sqlQuery, new EventMap());
    }

    @Override
    public Event addEvent(Event event) {
        log.info("Сохранение события: {}", event);
        event.setTimestamp(Instant.now().toEpochMilli());
        String sqlQuery = "INSERT INTO PUBLIC.feeds (timestamp, user_id, eventType_id, operation_id, entity_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"event_id"});
            stmt.setLong(1, event.getTimestamp());
            stmt.setInt(2, event.getUserId());
            stmt.setInt(3, event.getEventType().ordinal() + 1);
            stmt.setInt(4, event.getOperation().ordinal() + 1);
            stmt.setInt(5, event.getEntityId());
            return stmt;
        }, keyHolder);

        int userKey = keyHolder.getKey().intValue();

        return jdbcTemplate.queryForObject(sqlQueryGet + " WHERE event_id=?", new EventMap(), userKey);
    }

    @Override
    public Event update(Event event) {
        log.info("Обновление события: {}", event);
        String sqlQuery = "UPDATE PUBLIC.feeds SET timestamp=?, user_id=?, eventType_id=?, operation_id=?, entity_id=? WHERE event_id=?";
        jdbcTemplate.update(sqlQuery,
                event.getTimestamp(),
                event.getUserId(),
                event.getEventType().ordinal() + 1,
                event.getOperation().ordinal() + 1,
                event.getEntityId(),
                event.getEventId());
        return jdbcTemplate.queryForObject(sqlQueryGet, new EventMap(), event.getEventId());
    }

    @Override
    public List<Event> findEventsByUserId(int id) {
        log.info("Получить список событий по id пользователя: {}", id);
        String sqlQuery = sqlQueryGet + " WHERE user_id=?";
        return jdbcTemplate.query(sqlQuery, new EventMap(), id);
    }

    private final class EventMap implements RowMapper<Event> {

        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Event.builder()
                    .eventId(rs.getInt("event_id"))
                    .timestamp(rs.getLong("timestamp"))
                    .userId(rs.getInt("user_id"))
                    .eventType(EventType.valueOf(rs.getString("eventType")))
                    .operation(Operation.valueOf(rs.getString("operation")))
                    .entityId(rs.getInt("entity_id"))
                    .build();
        }
    }
}
