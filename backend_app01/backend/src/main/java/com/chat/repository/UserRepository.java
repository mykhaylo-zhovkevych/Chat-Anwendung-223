package com.chat.repository;


import com.chat.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository-Schnittstelle für den Zugriff auf Benutzer (Users).
 *
 * <p>Erweitert JpaRepository, um CRUD-Operationen für {@link Users} zu ermöglichen.</p>
 */
public interface UserRepository extends JpaRepository<Users, Long> {

    /**
     * Findet einen Benutzer anhand des eindeutigen Benutzernamens.
     *
     * @param username der Benutzername
     * @return der Benutzer mit dem angegebenen Benutzernamen oder {@code null}, falls nicht gefunden
     */
    Users findByUsername(String username);

    /**
     * Findet einen Benutzer anhand der eindeutigen E-Mail-Adresse.
     *
     * @param email die E-Mail-Adresse
     * @return der Benutzer mit der angegebenen E-Mail oder {@code null}, falls nicht gefunden
     */
    Users findByEmail(String email);
}
