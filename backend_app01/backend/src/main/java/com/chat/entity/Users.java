package com.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Repräsentiert einen Benutzer in der Anwendung.
 *
 * <p>Diese Klasse ist eine JPA-Entität und wird mit einer Datenbanktabelle verknüpft.
 * Sie enthält alle grundlegenden Informationen, die für einen Benutzer benötigt werden:
 * Benutzername, vollständiger Name, E-Mail-Adresse und Passwort.</p>
 *
 * <p>Die Annotationen von Lombok (@Data, @ToString, @NoArgsConstructor, @AllArgsConstructor)
 * generieren automatisch Getter, Setter, Konstruktoren und die toString-Methode.</p>
 *
 * <p>Die Felder <code>username</code> und <code>email</code> sind eindeutig.</p>
 *
 * @author
 */
@ToString
@Data
@Entity
public class Users {

    /**
     * Die eindeutige ID des Benutzers. Wird automatisch von der Datenbank generiert.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Der eindeutige Benutzername.
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * Der vollständige Name des Benutzers.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Die eindeutige E-Mail-Adresse des Benutzers.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Das Passwort des Benutzers. 
     * (Hinweis: In einer echten Anwendung sollte es gehasht gespeichert werden.)
     */
    @Column(nullable = false)
    private String password;

    /**
     * Standardkonstruktor (wird von JPA benötigt).
     */
    public Users() {}

    /**
     * Konstruktor zum Erstellen eines Benutzers mit allen erforderlichen Feldern.
     *
     * @param username der Benutzername
     * @param name der vollständige Name
     * @param email die E-Mail-Adresse
     * @param password das Passwort
     */
    public Users(String username, String name, String email, String password) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
