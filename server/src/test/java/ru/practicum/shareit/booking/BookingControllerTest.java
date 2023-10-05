package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    private static final String USER_HEADER = "X-Sharer-User-Id";
    @Mock
    private BookingService service;
    @InjectMocks
    private BookingController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;
    private BookingDto bookingDto;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        bookingDto = BookingDto.builder()
                .id(1)
                .item(ItemDto.builder().name("item").build())
                .booker(UserDto.builder().name("user").build())
                .status(Status.APPROVED)
                .build();
    }

    @Test
    void findAllByBookerAndState() throws Exception {
        when(service.findAllByBookerAndState(anyInt(), any(State.class), anyInt(), anyInt())).thenReturn(List.of(bookingDto));
        mockMvc.perform(get("/bookings")
                        .header(USER_HEADER, 1)
                        .param("state", State.PAST.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.[0].item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.[0].booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.[0].status", is(bookingDto.getStatus().name())));
    }

    @Test
    void findAllByOwnerAndState() throws Exception {
        when(service.findAllByOwnerAndState(anyInt(), any(State.class), anyInt(), anyInt())).thenReturn(List.of(bookingDto));
        mockMvc.perform(get("/bookings/owner")
                        .header(USER_HEADER, 1)
                        .param("state", State.PAST.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.[0].item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.[0].booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.[0].status", is(bookingDto.getStatus().name())));
    }

    @Test
    void findById() throws Exception {
        when(service.findById(anyInt(), anyInt())).thenReturn(bookingDto);
        mockMvc.perform(get("/bookings/1")
                        .header(USER_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())));
    }

    @Test
    void create() throws Exception {
        when(service.create(anyInt(), any(BookingDto.class))).thenReturn(bookingDto);
        mockMvc.perform(post("/bookings")
                        .header(USER_HEADER, 1)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())));
    }

    @Test
    void updateStatus() throws Exception {
        when(service.updateStatus(anyInt(), anyInt(), anyBoolean())).thenReturn(bookingDto);
        mockMvc.perform(patch("/bookings/1")
                        .header(USER_HEADER, 1)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())));
    }
}