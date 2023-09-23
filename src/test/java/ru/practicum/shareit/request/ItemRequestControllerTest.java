package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {

    private static final String USER_HEADER = "X-Sharer-User-Id";
    @Mock
    private ItemRequestService service;

    @InjectMocks
    private ItemRequestController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        itemRequestDto = ItemRequestDto.builder()
                .description("description")
                .build();
    }

    @Test
    void findAllByUserId() throws Exception {
        when(service.findAllByUserId(anyInt())).thenReturn(List.of(itemRequestDto));
        mockMvc.perform(get("/requests")
                        .header(USER_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("size()", is(1)))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDto.getDescription())));
    }

    @Test
    void findById() throws Exception {
        when(service.findById(anyInt(), anyInt())).thenReturn(itemRequestDto);
        mockMvc.perform(get("/requests/1")
                        .header(USER_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
    }

    @Test
    void findAllByParams() throws Exception {
        when(service.findAllByParams(anyInt(), any(Pageable.class))).thenReturn(List.of(itemRequestDto));
        mockMvc.perform(get("/requests/all")
                        .header(USER_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("size()", is(1)))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDto.getDescription())));
    }

    @Test
    void create() throws Exception {
        when(service.create(any(ItemRequestDto.class), anyInt())).thenReturn(itemRequestDto);
        mockMvc.perform(post("/requests")
                        .header(USER_HEADER, 1)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
    }
}