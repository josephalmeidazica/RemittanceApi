package com.api.remittance.Controllers;

import com.api.remittance.Entities.User;
import com.api.remittance.Exceptions.UserNotFoundException;
import com.api.remittance.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.Optional;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




class UserControllerTest {

    private UserRepository userRepository;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        UserController userController = new UserController(userRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new Object() {
                    // Simple ControllerAdvice for UserNotFoundException
                    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
                    public org.springframework.http.ResponseEntity<String> handle(UserNotFoundException ex) {
                        return org.springframework.http.ResponseEntity.notFound().build();
                    }
                })
                .build();
    }

    @Test
    void testAllUsers() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Alice");
        User user2 = new User();
        user2.setId(2L);
        user2.setName("Bob");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testNewUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Alice");

        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Alice\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Alice")));
    }

    @Test
    void testUpdateUser_Existing() throws Exception {
        User existing = new User();
        existing.setId(1L);
        existing.setName("OldName");

        User updated = new User();
        updated.setId(1L);
        updated.setName("NewName");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenReturn(updated);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"NewName\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NewName")));
    }

    @Test
    void testUpdateUser_New() throws Exception {
        User newUser = new User();
        newUser.setId(1L);
        newUser.setName("Created");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Created\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Created")));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userRepository).deleteById(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userRepository, times(1)).deleteById(1L);
    }
}