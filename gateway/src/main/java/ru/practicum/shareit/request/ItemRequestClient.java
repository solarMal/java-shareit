package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Service
public class ItemRequestClient extends BaseClient {

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/requests"))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> findAllByUser(Integer userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findById(Integer userId, Integer id) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> findAllByParams(Integer userId, int from, int size) {
        return get("/all?from=" + from + "&size=" + size, userId);
    }

    public ResponseEntity<Object> create(Integer userId, ItemRequestDto itemRequestDto) {
        return post("", userId, itemRequestDto);
    }
}