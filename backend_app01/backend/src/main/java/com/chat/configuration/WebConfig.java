package com.chat.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Konfigurationsklasse für die Webanwendung zur Definition globaler CORS-Richtlinien.
 * <p>
 * Diese Klasse implementiert das {@link WebMvcConfigurer}-Interface und definiert 
 * standortübergreifende Anfragen (CORS), die für die gesamte Anwendung gelten.
 * </p>
 *
 * <p>Erlaubt werden Anfragen von {@code http://localhost:3000} mit den HTTP-Methoden 
 * GET, POST, PUT und DELETE. Zudem sind alle Header erlaubt und Anfragen dürfen
 * Anmeldeinformationen (Cookies, Authentifizierung) enthalten.</p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Konfiguriert die CORS-Mappings für alle Endpunkte der Anwendung.
     *
     * @param registry das {@link CorsRegistry}-Objekt zur Registrierung von CORS-Konfigurationen.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Gilt für alle Pfade
                .allowedOrigins("http://localhost:3000") // Erlaubte Ursprungsadresse
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Erlaubte HTTP-Methoden
                .allowedHeaders("*")
                .allowCredentials(true);
             
    }
}