package com.chat.controller;

import com.chat.entity.ChatMessage;
import com.chat.model.LoginRequest;
import com.chat.model.Message;
import com.chat.model.UserDto;
import com.chat.repository.ChatMessageRepository;
import com.chat.repository.UserRepository;
import com.chat.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.Map; 

import java.util.List;

import com.chat.entity.Users;

/**
 * REST- und WebSocket-Controller für Benutzer- und Chat-Funktionalitäten.
 * 
 * <p>Diese Klasse behandelt die Registrierung, Anmeldung, Benutzersuche sowie das Senden
 * von öffentlichen und privaten Chatnachrichten.</p>
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;

    /**
     * Führt die Benutzeranmeldung durch.
     * 
     * @param loginRequest enthält Benutzername und Passwort
     * @param session HTTP-Session zur Speicherung des eingeloggten Benutzers
     * @return HTTP 200 bei Erfolg, 404 wenn Benutzer nicht gefunden, 401 bei falschem Passwort
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            Users user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            session.setAttribute("user", user);
            return ResponseEntity.ok("Login successful");
        } catch (RuntimeException ex) {
            if ("User not found".equals(ex.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            if ("Invalid password".equals(ex.getMessage())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    /**
     * Führt die Benutzerabmeldung durch.
     * 
     * @param session HTTP-Session, die invalidiert wird
     * @return HTTP 200 bei erfolgreicher Abmeldung
     */
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }

    /**
     * Sucht einen Benutzer anhand des Benutzernamens.
     * 
     * @param username der Benutzername
     * @return Benutzerobjekt oder 404, falls nicht gefunden
     */
    @GetMapping("/search")
    public ResponseEntity<Users> findByUsername(@RequestParam String username) {
        Users user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Registriert einen neuen Benutzer.
     * 
     * @param userDto enthält Benutzerdaten wie Name, E-Mail, Passwort usw.
     * @return HTTP 200 bei erfolgreicher Registrierung
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto userDto) {
        try {
            userService.signUpUser(
                    userDto.getUsername(),
                    userDto.getName(),
                    userDto.getEmail(),
                    userDto.getPassword()
            );
            return ResponseEntity.ok("User created successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    /**
     * Empfängt eine öffentliche Chatnachricht über WebSocket.
     * 
     * <p>Die Nachricht wird gespeichert und an alle Clients im Chatroom gesendet.</p>
     *
     * @param message das empfangene Nachrichtenobjekt
     * @return das weitergeleitete Nachrichtenobjekt
     * @throws InterruptedException falls beim Verarbeiten eine Unterbrechung auftritt
     */
    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(Message message) throws InterruptedException {
        chatMessageRepository.save(new ChatMessage(
                message.getSenderName(),
                message.getReceiverName(),
                message.getMessage(),
                message.getMedia(),
                message.getMediaType(),
                message.getStatus(),
                System.currentTimeMillis()
        ));
        return message;
    }

    /**
     * Sendet eine private Chatnachricht über WebSocket an einen bestimmten Benutzer.
     * 
     * <p>Die Nachricht wird auch in der Datenbank gespeichert.</p>
     *
     * @param message die zu sendende private Nachricht
     */
    @MessageMapping("/private-message")
    public void privateMessage(Message message) {
        String receiver = message.getReceiverName();
        simpMessagingTemplate.convertAndSendToUser(receiver, "/private", message);

        chatMessageRepository.save(new ChatMessage(
                message.getSenderName(),
                message.getReceiverName(),
                message.getMessage(),
                message.getMedia(),
                message.getMediaType(),
                message.getStatus(),
                System.currentTimeMillis()
        ));
    }

    /**
     * Führt die Benutzeranmeldung durch und gibt einen JWT zurück.
     *
     * @param loginRequest enthält Benutzername und Passwort
     * @return JWT als String bei Erfolg, sonst Fehlerstatus
     */
    @PostMapping("/jwt-login")
    public ResponseEntity<?> jwtLogin(@RequestBody LoginRequest loginRequest) {
        try {
            String token = userService.authenticateAndGenerateToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            );
            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falsche Anmeldedaten");
        }
    }

    /**
     * Ruft den gesamten Chatverlauf zwischen zwei Benutzern ab.
     *
     * @param user1 Benutzername des ersten Benutzers
     * @param user2 Benutzername des zweiten Benutzers
     * @return Liste der zwischen beiden Benutzern ausgetauschten Nachrichten
     */
    @GetMapping("/history/{user1}/{user2}")
    public ResponseEntity<List<ChatMessage>> getChatHistory(
            @PathVariable String user1,
            @PathVariable String user2
    ) {
        List<ChatMessage> messages = chatMessageRepository.findByReceiverNameOrSenderName(user1, user2);
        return ResponseEntity.ok(messages);
    }
}