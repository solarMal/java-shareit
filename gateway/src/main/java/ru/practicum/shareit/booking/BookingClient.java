package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.client.BaseClient;

@Service
public class BookingClient extends BaseClient {

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/bookings"))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> findAllByBookerAndState(Integer userId, State state, Integer from, Integer size) {
        return get("?state=" + state + "&from=" + from + "&size=" + size, userId);
    }

    public ResponseEntity<Object> findAllByOwnerAndState(Integer userId, State state, Integer from, Integer size) {
        return get("/owner?state=" + state + "&from=" + from + "&size=" + size, userId);
    }

    public ResponseEntity<Object> findById(Integer bookingId, Integer userId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> create(BookingDto bookingDto, Integer userId) {
        return post("", userId, bookingDto);
    }

    public ResponseEntity<Object> updateStatus(Integer userId, Integer bookingId, Boolean approved) {
        return patch("/" + bookingId + "?approved=" + approved, userId, null, null);
    }
}