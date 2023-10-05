package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Service
public class ItemClient extends BaseClient {

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/items"))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> create(Integer userId, ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> update(ItemDto itemDto, Integer itemId, Integer userId) {
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> findById(Integer itemId, Integer userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> findAllByUserId(Integer userId) {
        return get("/", userId);
    }

    public ResponseEntity<Object> findByText(String text) {
        return get("/search?text=" + text);
    }

    public ResponseEntity<Object> addComment(Integer itemId, Integer userId, CommentDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}