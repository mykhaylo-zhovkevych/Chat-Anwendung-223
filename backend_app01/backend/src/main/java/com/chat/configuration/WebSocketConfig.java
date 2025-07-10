package com.chat.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Konfigurationsklasse für WebSocket mit STOMP in der Chat-Anwendung.
 * <p>
 * Diese Klasse aktiviert die Unterstützung für WebSocket-Nachrichten über STOMP 
 * (Simple Text Oriented Messaging Protocol) und konfiguriert die entsprechenden 
 * Endpunkte sowie die Message-Broker.
 * </p>
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Registriert den STOMP-Endpunkt, über den Clients eine WebSocket-Verbindung herstellen können.
     * <p>
     * Der Endpunkt {@code /ws} unterstützt SockJS als Fallback für Browser, die kein natives WebSocket unterstützen.
     * Alle Ursprünge werden zugelassen (CORS: {@code *}).
     * </p>
     *
     * @param registry die STOMP-Endpunkt-Registrierung
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // SockJS-Fallback aktivieren
    }

    /**
     * Konfiguriert den Nachrichtentransport über STOMP.
     * <p>
     * - Präfix {@code /app} für Nachrichten, die an @MessageMapping-Methoden gesendet werden.<br>
     * - Aktiviert einen einfachen Broker mit den Zielpräfixen {@code /chatroom} und {@code /user}.<br>
     * - Setzt das Benutzerziel-Präfix auf {@code /user}, um private Nachrichten zu ermöglichen.
     * </p>
     *
     * @param registry die Broker-Registrierung
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/chatroom", "/user");
        registry.setUserDestinationPrefix("/user");
    }

    /**
     * Konfiguriert technische Parameter für den WebSocket-Nachrichtentransport.
     * <p>
     * Diese Einstellungen erlauben größere Nachrichten und unbegrenzte Sendezeit:
     * <ul>
     *     <li>Sendezeitlimit: 0 (unbegrenzt)</li>
     *     <li>Sendepuffergröße: 50 MB</li>
     *     <li>Maximale Nachrichtengröße: 50 MB</li>
     * </ul>
     * </p>
     *
     * @param registry die Transport-Registrierung
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setSendTimeLimit(0)
                .setSendBufferSizeLimit(50 * 1024 * 1024)
                .setMessageSizeLimit(50 * 1024 * 1024);
    }
}
