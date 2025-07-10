package com.chat.service;

import com.chat.entity.Users;
import com.chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service-Klasse für Benutzerverwaltung.
 * 
 * <p>Diese Klasse kapselt die Geschäftslogik zur Verwaltung von Benutzern,
 * z.B. Registrierung und Suche nach Benutzern.</p>
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Registriert einen neuen Benutzer.
     * 
     * <p>Prüft, ob der Benutzername oder die E-Mail bereits existieren, 
     * und wirft sonst eine RuntimeException.</p>
     *
     * @param username der gewünschte Benutzername
     * @param name     der vollständige Name des Benutzers
     * @param email    die E-Mail-Adresse des Benutzers
     * @param password das Passwort des Benutzers
     * @return das gespeicherte {@link Users}-Objekt
     * @throws RuntimeException wenn Benutzername oder E-Mail bereits vergeben sind
     */
    public Users signUpUser(String username, String name, String email, String password) {
        if (userRepository.findByUsername(username) != null) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("Email already exists");
        }

        Users newUser = new Users(username, name, email, password);

        return userRepository.save(newUser);
    }

    /**
     * Findet einen Benutzer anhand seines Benutzernamens.
     *
     * @param username der Benutzername
     * @return das gefundene {@link Users}-Objekt oder {@code null}, falls nicht gefunden
     */
    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
