package com.chat.repository;

import com.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository-Schnittstelle für den Zugriff auf Chat-Nachrichten.
 * 
 * <p>Erweitert JpaRepository, um CRUD-Operationen für {@link ChatMessage} zu ermöglichen.</p>
 */
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * Findet alle Chatnachrichten, bei denen der Benutzer entweder Empfänger oder Absender ist.
     *
     * @param receiverName der Name des Empfängers
     * @param senderName   der Name des Absenders
     * @return Liste der gefundenen Chatnachrichten
     */
    List<ChatMessage> findByReceiverNameOrSenderName(String receiverName, String senderName);
}
