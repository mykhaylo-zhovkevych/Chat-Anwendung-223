package com.chat.entity;

import com.chat.model.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Repräsentiert eine Chatnachricht zwischen zwei Benutzern.
 *
 * <p>Diese Klasse ist eine JPA-Entität und wird in der Datenbank persistiert. 
 * Sie enthält Informationen über Absender, Empfänger, Nachrichtentext, Anhänge, Medientyp, Status und Zeitstempel.</p>
 *
 * <p>Lombok erzeugt automatisch Getter, Setter, equals, hashCode und toString über {@code @Data} sowie einen 
 * parameterlosen Konstruktor über {@code @NoArgsConstructor}.</p>
 */
@Data
@NoArgsConstructor
@Entity
public class ChatMessage {

    /**
     * Die eindeutige ID der Nachricht (Primärschlüssel).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Benutzername des Absenders der Nachricht.
     */
    private String senderName;

    /**
     * Benutzername des Empfängers der Nachricht.
     */
    private String receiverName;

    /**
     * Der eigentliche Nachrichtentext.
     */
    private String message;

    /**
     * Optional: Pfad oder URL zu einem beigefügten Medium (z. B. Bild, Video, Datei).
     */
    private String media;

    /**
     * Der Typ des Mediums (z. B. "image/png", "video/mp4").
     */
    private String mediaType;

    /**
     * Status der Nachricht (z. B. GESENDET, EMPFANGEN, GELESEN).
     */
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * Zeitstempel der Nachricht (z. B. Unix-Zeit in Millisekunden).
     * Darf nicht null sein.
     */
    @Column(nullable = false)
    private Long timestamp;

    /**
     * Konstruktor zum Erstellen einer neuen Chatnachricht.
     *
     * @param senderName   Benutzername des Absenders
     * @param receiverName Benutzername des Empfängers
     * @param message      Inhalt der Nachricht
     * @param media        Pfad/URL zum Medium (optional)
     * @param mediaType    Medientyp (optional)
     * @param status       Status der Nachricht
     * @param timestamp    Zeitpunkt der Nachricht (in Millisekunden)
     */
    public ChatMessage(String senderName, String receiverName, String message, String media,
                       String mediaType, Status status, Long timestamp) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.message = message;
        this.media = media;
        this.mediaType = mediaType;
        this.status = status;
        this.timestamp = timestamp;
    }
}
