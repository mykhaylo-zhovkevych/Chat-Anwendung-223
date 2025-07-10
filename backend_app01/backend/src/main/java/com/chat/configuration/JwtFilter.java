package com.chat.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter zur Validierung von JWT-Token für geschützte API-Endpunkte.
 * <p>
 * Nur Anfragen, deren Pfade mit {@code /api/secure/} beginnen, werden überprüft.
 * Für alle anderen Endpunkte wird der Filter übersprungen.
 */
public class JwtFilter extends OncePerRequestFilter {

    /**
     * Prüft bei geschützten Routen den Authorization-Header und validiert das JWT.
     *
     * @param request      HTTP-Anfrage
     * @param response     HTTP-Antwort
     * @param filterChain  Weitergabe an den nächsten Filter in der Kette
     * @throws ServletException falls ein Servlet-Fehler auftritt
     * @throws IOException      falls ein I/O-Fehler auftritt
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Nur Token prüfen bei Pfaden, die mit /api/secure/ beginnen
        if (path.startsWith("/api/secure/")) {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Fehlender oder ungültiger Authorization Header");
                return;
            }

            String jwt = authHeader.substring(7);
            if (!JwtUtil.validateToken(jwt)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Ungültiges oder abgelaufenes Token");
                return;
            }
        }

        // Wenn alles passt oder Pfad nicht geschützt ist → weiterreichen
        filterChain.doFilter(request, response);
    }
}
