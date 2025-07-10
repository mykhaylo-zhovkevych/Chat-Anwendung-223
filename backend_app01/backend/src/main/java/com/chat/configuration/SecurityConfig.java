package com.chat.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Konfiguriert die Sicherheitsregeln für die Webanwendung.
 * 
 * <p>Diese Klasse definiert, welche Endpunkte öffentlich zugänglich sind
 * und welche nur mit einem gültigen JWT aufrufbar sind.</p>
 *
 * <p>Die Klasse registriert ausserdem den {@link JwtFilter}, um JWT-Token bei
 * geschützten Anfragen vor der Verarbeitung zu validieren.</p>
 *
 * <p>Die Pfade unter <code>/api/secure/**</code> sind geschützt und erfordern
 * ein gültiges JWT im <code>Authorization</code>-Header. Andere Pfade wie
 * <code>/login</code> oder <code>/signup</code> sind öffentlich erreichbar.</p>
 *
 * @author
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Konfiguriert den HTTP-Sicherheitsfilter für die Anwendung.
     *
     * <p>Öffentliche Endpunkte (z. B. Anmeldung und Registrierung) sind ohne
     * Authentifizierung erreichbar. Alle Endpunkte unter <code>/api/secure/**</code>
     * sind durch JWT-basierte Authentifizierung geschützt.</p>
     *
     * @param http das {@link HttpSecurity}-Objekt zur Konfiguration
     * @return ein {@link SecurityFilterChain}-Bean, das die Sicherheitsregeln definiert
     * @throws Exception bei Konfigurationsfehlern
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                    // Öffentliche Endpunkte
                    .requestMatchers(
                        "/api/users/login",
                        "/api/users/jwt-login",
                        "/api/users/signup",
                        "/api/users/search",
                        "/api/users/history/**",
                        "/ws/**"
                    ).permitAll()
                    // Gesicherte Endpunkte erfordern JWT
                    .requestMatchers("/api/secure/**").authenticated()
                )
                // JWT-Filter vor UsernamePasswordAuthenticationFilter einfügen
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}