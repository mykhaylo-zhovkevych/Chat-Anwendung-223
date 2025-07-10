package com.chat.configuration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Dienstklasse zur Erstellung und Validierung von JWT-Token (JSON Web Token).
 * <p>
 * Diese Utility-Klasse ermöglicht das:
 * <ul>
 *     <li>Erzeugen eines JWT-Tokens mit Ablaufdatum</li>
 *     <li>Extrahieren des Benutzernamens (Subject) aus einem Token</li>
 *     <li>Validieren der Signatur und Gültigkeit eines Tokens</li>
 * </ul>
 * <p>
 * Wird typischerweise für Authentifizierung und Autorisierung in REST-APIs verwendet.
 */
public class JwtUtil {

    /**
     * Der geheime Schlüssel für die Signatur des Tokens (muss mindestens 256 Bit lang sein für HS256).
     */
    private static final String SECRET_KEY = "supergeheimespasswort1234567890123456";

    /**
     * Erzeugt ein JWT-Token für den angegebenen Benutzernamen.
     *
     * @param username Der Benutzername, der im Token gespeichert wird (Subject).
     * @return Ein signierter JWT-Token, gültig für 24 Stunden.
     */
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 Tag
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrahiert den Benutzernamen (Subject) aus einem gültigen JWT-Token.
     *
     * @param token Der JWT-Token.
     * @return Der im Token gespeicherte Benutzername.
     * @throws io.jsonwebtoken.JwtException wenn der Token ungültig oder abgelaufen ist.
     */
    public static String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Überprüft, ob ein JWT-Token gültig ist (Signatur und Ablaufzeit).
     *
     * @param token Der zu überprüfende JWT-Token.
     * @return {@code true}, wenn der Token gültig ist, sonst {@code false}.
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
