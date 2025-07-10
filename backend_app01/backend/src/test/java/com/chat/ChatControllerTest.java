package com.chat;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.chat.controller.ChatController;
import com.chat.entity.Users;
import com.chat.model.LoginRequest;
import com.chat.repository.UserRepository;
import com.chat.service.UserService;

public class ChatControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private ChatController chatController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void login_Successful() {
        String username = "testuser";
        String password = "password123";

        Users user = new Users(username, "Test User", "test@example.com", password);
        when(userRepository.findByUsername(username)).thenReturn(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        ResponseEntity<?> response = chatController.login(loginRequest, session);

        assertEquals(200, response.getStatusCodeValue());
        verify(session, times(1)).setAttribute("user", user);
    }

    @Test
    public void findByUsername_UserNotFound() {
        String username = "unknown";

        when(userService.findByUsername(username)).thenReturn(null);

        ResponseEntity<Users> response = chatController.findByUsername(username);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}