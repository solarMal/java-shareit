package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.user.UserRepository;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository repository;

    @Test
    void handleNotFoundGet() throws Exception {
        when(repository.findById(1)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void handleNotFoundPatch() throws Exception {
        when(repository.findById(1)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1")
                        .contentType("application/json")
                        .content("{\"name\": \"Test\", \"email\": \"test@test.com\"}"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));
    }
}
