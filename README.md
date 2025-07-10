# Dokumentation des Projektarbeits

## Pictures
_alte Design_
![Screenshot 2025-07-10 020128](https://github.com/user-attachments/assets/d5389bba-e397-410c-933d-817fb38d43f6)
![Screenshot 2025-07-09 213453](https://github.com/user-attachments/assets/6a0e6905-205a-4db1-9dd6-31ebf017576f)
![Screenshot 2025-07-09 213343](https://github.com/user-attachments/assets/62c1d959-8ae9-40d7-a6f6-82f2235b8a90)
![Screenshot 2025-07-09 212806](https://github.com/user-attachments/assets/cd1d09dd-9bf8-480d-b6a9-9bc420938401)


## Präludium

In dieser Projektarbeit wollte ich eine einfache Chat-Anwendung erstellen, in der die Nutzer miteinander kommunizieren können, sowohl in einem gemeinsamen Raum als auch einzeln. Die Voraussetzung war, dass Nachrichten und Bewegungen in einer relationalen Datenbank gespeichert werden, sodass später der Administrator nachvollziehen kann, wer mit wem kommuniziert hat oder wer sich eingeloggt hat.

####  **Arbeitsplan (2 Tage – ca. 20 Stunden)**

##### **Tag 1 – Planung & Vorbereitung** (ca. 10 Stunden)

**Ziele:**

- Projektziele klären und dokumentieren
- Technologie-Stack definieren (Spring Boot, MySQL, Next.js, WebSocket, JWT)
- User Stories mit Akzeptanzkriterien formulieren
- Sicherheitsanforderungen analysieren
- Datenbankmodell entwerfen
- Entwicklungsumgebung einrichten (IDE, Docker, Git)

**Erreichte Meilensteine:**

- Projektstruktur mit `frontend_app01/` und `backend_app01/` erstellt
- Datenbankverbindung lokal getestet (MySQL) 
- Erste User Stories definiert und diskutiert

**Reflexion:**  
Die Planungsphase war hilfreich, um einen klaren Überblick über das Projekt zu gewinnen. Besonders das Schreiben der User Stories hat dabei geholfen, sich in die Perspektive der Nutzer zu versetzen. Die grösste Herausforderung war, die verschiedenen Technologien sinnvoll zu kombinieren, aber durch gute Vorbereitung konnte ich eine stabile Grundlage schaffen.

---

######  **Tag 2 Teil1 – Realisierung: Backend & Authentifizierung** (ca. 10 Stunden)

**Ziele:**

- Spring Boot Backend aufsetzen
- Datenbankmodell mit JPA Entities umsetzen
- REST API für User-Registrierung, Login, Logout, Suche
- JWT-Authentifizierung implementieren
- Grundlegendes Sicherheitskonzept dokumentieren

**Erreichte Meilensteine:**

- Login & Signup funktionieren über REST
- JWT wird generiert und validiert
- Sicherheitsfilter (JWT-Filter) schützt private Endpunkte
- REST-Routen mit Swagger/Postman getestet

**Reflexion:**  
Die grösste Herausforderung war die korrekte Integration des JWT-Filters. Nachdem das aber funktioniert hat, lief die Authentifizierung stabil. Ich habe bewusst Zeit in die Dokumentation des Sicherheitskonzepts investiert – das war wichtig für spätere Erweiterungen. Das Backend war zum Ende des Tages stabil.

###### Teil 02: Frontend & WebSocket-Chat

**Ziele:**
- Next.js Frontend aufbauen
- Login-Formular und Signup-Formular mit Fehlerhandling
- User-Suche und Anzeige der Chatpartner
- Öffentliche und private WebSocket-Kommunikation mit STOMP
- Speicherung der Nachrichten im Backend

**Erreichte Meilensteine:**

- UI für Login/Signup mit Axios und Validation
- WebSocket funktioniert mit Spring Boot Backend
- Nachrichten werden in der Datenbank gespeichert
- Private Chats werden gespeichert und angezeigt

**Reflexion:**  
Besonders das Zusammenspiel von Frontend und WebSocket war eine spannende Herausforderung. Es hat Spass gemacht, Live-Kommunikation zwischen Nutzern zu sehen. Auch die Nachrichtenspeicherung im Backend funktionierte wie geplant. Am Ende konnte ich einen stabilen Chat zwischen zwei und mehrere Nutzern inkl. Nachrichtenhistorie für private Chats umsetzen.



#### User Stories mit Akzeptanzkriterien

1. **Als Nutzer möchte ich ein neues Konto anlegen können**,  
    damit ich mich registrieren und mit anderen Benutzern chatten kann.  
    **Akzeptanzkriterium:** Die Registrierung ist erfolgreich, wenn Benutzername, E-Mail und Passwort gültig sind und der Benutzer in der Datenbank gespeichert wird.
    

---

2. **Als Nutzer möchte ich mich per JWT oder Session anmelden können**,  
    damit ich dauerhaft eingeloggt bleibe und mich nicht jedes Mal neu anmelden muss.  
    **Akzeptanzkriterium:** Nach dem Login erhält der Nutzer ein gültiges JWT-Token oder eine Session, die automatisch bei weiteren Anfragen verwendet wird.
    

---

3. **Als Nutzer möchte ich in einem öffentlichen Chatraum schreiben und Nachrichten von anderen lesen können**,  
    damit ich mich mit allen aktiven Nutzern austauschen kann.  
    **Akzeptanzkriterium:** Nachrichten erscheinen in Echtzeit für alle eingeloggten Nutzer im öffentlichen Chatbereich.
    

---

4. **Als Nutzer möchte ich andere Nutzer suchen und ihnen private Nachrichten senden können**,  
    damit ich direkt mit einzelnen Personen kommunizieren kann.  
    **Akzeptanzkriterium:** Die Suche liefert passende Nutzer, und private Nachrichten erscheinen nur bei Sender und Empfänger.
    

---

5. **Als Nutzer möchte ich auf vergangene private Chats zugreifen können**,  
    damit ich ältere Unterhaltungen einsehen kann.  
    **Akzeptanzkriterium:** Frühere Chatverläufe zwischen zwei Nutzern sind abrufbar und chronologisch sortiert.



#### Ein paar Worte zu den Transaktionen und dem Archetekut der Anwendung 
-  In dieser Anwendung wird jede Interaktion zwischen Benutzern als Transaktion betrachtet, und was die Backend-Architektur betrifft: 
###### Welche Architekturform nutzt du?

- Dein Backend ist eine **klassische mehrschichtige Architektur** (Layered Architecture).
- Dein Frontend ist eine **Single-Page Application (SPA)** mit Next.js, die über APIs mit dem Backend kommuniziert.
- Zwischen Frontend und Backend läuft die Kommunikation via REST-API und WebSocket.


 Die Grundidee der Schichten in Backend:

- **Präsentationsschicht (Controller)**: Diese Schicht ist für den Empfang von Anfragen zuständig, z. B. wenn dein Next.js-Frontend eine HTTP-Anfrage (REST-API) oder eine WebSocket-Nachricht sendet. Der Controller nimmt die Anfrage entgegen, leitet sie weiter und gibt die Antwort zurück.
- **Geschäftsschicht (Service)**: Hier passiert die eigentliche „Logik“ deiner Anwendung – also Regeln, Berechnungen oder das Steuern von Abläufen. Du hältst die Logik vom Controller getrennt, damit die Präsentation (z. B. Web-Frontend) unabhängig von der Business-Logik bleibt.
- **Datenzugriffsschicht (Repository)**: Diese Schicht ist für die Kommunikation mit der Datenbank zuständig. Du benutzt z. B. JPA-Repositorys, um Daten zu speichern, zu suchen oder zu löschen. Der Service fragt diese Schicht an, wenn er Daten braucht oder speichern will.
- **Domänenschicht (Entity/Model)**: Das sind die Datenmodelle meiner Anwendung, also die Klassen, die deine Datenstruktur repräsentieren (z. B. User oder ChatMessage). Diese Klassen entsprechen typischerweise den Datenbanktabellen.

Mein Frontend gliedert sich in folgende Bereiche:

- **UI-Komponenten**  
    Du baust viele wiederverwendbare Komponenten (z. B. Login-Formular, Chatfenster, Suchleiste). Diese Komponenten kapseln Darstellung und Interaktion und sind modular, damit du sie einfach an verschiedenen Stellen einsetzen kannst.
- **Seiten (Pages)**  
    In Next.js repräsentieren Seiten die einzelnen Routen deiner Anwendung, z. B. die Login-Seite, die Chat-Seite oder die Startseite. Next.js sorgt automatisch dafür, dass diese URLs zur passenden Komponente führen.
- **State Management**  
    Der Zustand (State) deiner Anwendung – z. B. wer gerade eingeloggt ist, welche Nachrichten angezeigt werden oder welcher User ausgewählt ist – wird in React-Hooks wie `useState`, `useEffect` oder Context verwaltet. Das ermöglicht reaktive Updates der UI, wenn sich Daten ändern.
- **API-Kommunikation**  
    Deine Frontend-Komponenten kommunizieren mit dem Backend über REST-APIs (z. B. Login, Benutzerregistrierung) und WebSockets (für den Echtzeit-Chat). Hierfür nutzt du Bibliotheken wie `axios` für HTTP-Anfragen und `stompjs` / `sockjs-client` für WebSocket-Verbindungen.
- **Routing und Navigation**  
    Next.js übernimmt das Routing (Wechsel zwischen Seiten) automatisch. Du kannst z. B. nach erfolgreichem Login mit dem Router zur Chat-Seite navigieren. Auch dynamische Routen (z. B. Chat mit verschiedenen Usern) sind einfach realisierbar.
- **Client-seitige Logik**  
    Funktionen wie das Verarbeiten von Benutzereingaben, Absenden von Nachrichten oder das Verwalten von WebSocket-Verbindungen sind in React-Komponenten oder eigenen Hooks gekapselt.

Die Frontend Architektur folgt einer **Component-Based Architecture** mit dem SPA-Prinzip:

- **Komponenten als Bausteine**: Die UI ist in kleine, gut wiederverwendbare Einheiten zerlegt, die jeweils nur für ihre eigene Darstellung und Logik verantwortlich sind.
- **Reaktive Datenflüsse**: Wenn sich Zustände ändern (z. B. neue Chat-Nachricht), aktualisiert React automatisch die Darstellung.
- **Trennung von Darstellung und Daten**: Die API-Aufrufe und WebSocket-Verbindungen sind klar getrennt von der UI, sodass die Komponenten nur ihre Daten bekommen und anzeigen müssen.
- **Unidirektionaler Datenfluss**: Daten fliessen meist von oben nach unten (Parent zu Child), was die Kontrolle und Vorhersagbarkeit der Anwendung erhöht.


---

####  **Teil 1: Auswertung mit Soll-Ist-Vergleich & Problemanalyse**

Eine **Auswertung mit objektivem Soll-Ist-Vergleich** besteht aus drei Schritten:

#####  **1. Zieldefinition (SOLL-Zustand)**

Was war ursprünglich geplant?

- Die Anwendung soll über eine Login-Funktion mit JWT verfügen.
- Daten sollen über eine REST-API gespeichert werden.
- Der Chat soll in Echtzeit über WebSockets funktionieren.
- Das Frontend (Next.js) und Backend (Spring Boot) sollen separat in Containern laufen.
- Das Design soll angenehmer werden, für die Users.

##### **2. Tatsächlicher Zustand (IST-Zustand)**

Was wurde erreicht? Was funktioniert?

- Login mit JWT funktioniert ✅
- REST-API speichert Benutzer und Nachrichten korrekt ✅
- WebSocket-Chat funktioniert perfekt✅
- Docker-Container starten ✅
- Leider nein, aufgrund eines Managementproblems, aber sehr einfach anzupassen ❌ 

---

#####  **3. Problemanalyse**

Was ist die Ursache für Abweichungen?

- ❌ WebSocket-Verzögerung: evtl. durch fehlendes Caching oder langsame Verbindungen(sher ).
    
- ❌ Docker-Probleme: Spring Boot greift auf `localhost` zu, statt auf Service-Namen (`mysql_db`) und umgekehrt. (wenn die App in config falschen Variable hat)

##### ✅ **Teil 2: Sicherheitskonzept dokumentieren**

Ein **Sicherheitskonzept** enthält Massnahmen auf verschiedenen Ebenen. Hier ist ein Beispiel für meine Anwendung:

##### **1. Authentifizierung & Autorisierung**

- JWT wird verwendet → Sessions sind stateless ✅    
- JWT wird geprüft im Filter (`JwtFilter`) für `/api/secure/**` ✅
- **Risiko**: JWT kann abgefangen werden → HTTPS dringend erforderlich 
    

##### 🔒 **2. Datenbankzugriff**

- MySQL ist mit Nutzer `bank` und Passwort `bank` gesichert
-  **Meine Empfehlung**: sichere Passwörter + Zugriff nur aus Docker-Netzwerk erlauben.

##### 🔒 **3. Netzwerk & Kommunikation**

- **Meine Empfehlung**:
    
    - Backend & Frontend nur über HTTPS bereitstellen
    - WebSocket ebenfalls mit `wss://` nutzen
    - In Docker nur nötige Ports freigeben

##### 🔒 **4. Validierung & Fehlerbehandlung**

- Aktuell:
    
    - Passwort wird nicht gehasht → **grosses Sicherheitsrisiko!**
    - Fehler werden teilweise abgefangen, aber nicht zentral protokolliert

- **Meine Empfehlung**:
    
    - Passwort mit `BCrypt` hashen
    - Globale Error-Handling-Klasse in Spring verwenden (`@ControllerAdvice`)

#### Deployment und Testing Backend

``` java
package com.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpSession;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.chat.controller.ChatController;
import com.chat.entity.Users;
import com.chat.model.LoginRequest;
import com.chat.repository.UserRepository;
import com.chat.service.UserService;

public class ChatControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private ChatController chatController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void login_Successful() {
        String username = "testuser";
        String password = "password123";

        Users user = new Users(username, "Test User", "test@example.com", password);
        when(userRepository.findByUsername(username)).thenReturn(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        ResponseEntity<?> response = chatController.login(loginRequest, session);

        assertEquals(200, response.getStatusCodeValue());
        verify(session, times(1)).setAttribute("user", user);
    }

    @Test
    public void findByUsername_UserNotFound() {
        String username = "unknown";

        when(userService.findByUsername(username)).thenReturn(null);

        ResponseEntity<Users> response = chatController.findByUsername(username);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}
```

In Backend habe ich zwei Testfälle gemacht: 
1. login_Successful, Ob der Login mit gültigen Benutzerdaten funktioniert. Moch für User Erstellung, dann wird eine Login Anfrage erstellt, am Ende wird getestet, ob dieser Session der Benutzer richtig gespeichert.
2. findByUsername_UserNotFound, Es wird getestet, was passiert, wenn nach einem Benutzer gesucht wird, der **nicht existiert** (z. B. „unknown“).  Am Ende wird geprüft: Ob der HTTP-Status 404 zurückgegeben wird, was bedeutet: Benutzer wurde nicht gefunden.


Für die Deployment habe ich zwei Dockerfile erstellt und eine dockerfile-compose, der alles bindet. 
``` Dockerfile
services:
  backend:
    build:
      context: ./backend_app01/backend
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql_db:3306/chat
      SPRING_DATASOURCE_USERNAME: bank
      SPRING_DATASOURCE_PASSWORD: bank
    depends_on:
      - mysql_db

  frontend:
    build:
      context: ./frontend_app01/chat
    ports:
      - "3000:3000"
    environment:
      - NEXT_PUBLIC_BACKEND_URL=http://localhost:8080

  mysql_db:
    image: mysql:8.0
    restart: always
    environment:
      MYSQL_DATABASE: chat
      MYSQL_ROOT_PASSWORD: root
      MYSQL_USER: bank
      MYSQL_PASSWORD: bank
    ports:
      - "3306:3306"
    volumes:
      - db_data:/var/lib/mysql

volumes:
  db_data:
```
Dieser Docker-Compose definiert drei Dienste: ein Backend auf Port 8080, das mit einer MySQL-Datenbank verbunden ist; ein Frontend auf Port 3000, das auf das Backend zugreift; und die MySQL-Datenbank selbst mit Benutzer und Passwort-Konfiguration sowie persistentem Speicher. Die Dienste starten in der Reihenfolge, wobei das Backend erst startet, wenn die Datenbank bereit ist.

Für die Manuelle Testing bitte, in Frontend npm install/npm run dev und in Backend mvn spring-boot:run ausführen.

#### Meine Reflektionen 
Ich bin grundsätzlich positiv gestimmt gegenüber meinem Projekt und mit den bisherigen Ergebnissen zufrieden. Obwohl ich mir gewünscht hätte, das Projekt noch professioneller umzusetzen, haben persönliche Stimmungsschwankungen und ein nicht optimales Zeitmanagement dazu geführt, dass ich nicht das volle Potenzial ausschöpfen konnte. Die erste Version des Frontend-Designs war stilistisch bereits ansprechend, jedoch habe ich leider durch Nachlässigkeit den Quellcode verloren und musste  komplett wieder neu entwickeln.
Meiner Meinung nach ist das Projekt noch nicht vollständig abgeschlossen. Ich plane, es in meiner Freizeit weiter auszubauen, um es skalierbarer zu machen, die Sicherheit zu erhöhen und es als Hobbyprojekt auch im Alltag nutzbar zu gestalten. Am Ende bin ich trotzdem zufrieden, da ich alle meine grundlegenden Voraussetzungen erreicht habe.



###### _Zusätzlich: Technischen Dokumentation_ 

JWT - Authentifizierung
### 🔐 1. Benutzer meldet sich über das Frontend an

In deiner Funktion `handleJwtLogin()` im Frontend: Du schickst **Username und Passwort** im Body an `POST /api/users/jwt-login`.

###### 2. Der Server prüft die Zugangsdaten

In deinem Spring-Controller: 
``` java
@PostMapping("/jwt-login")
public ResponseEntity<?> jwtLogin(@RequestBody LoginRequest loginRequest) {
    Users user = userRepository.findByUsername(loginRequest.getUsername());
    if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falsche Anmeldedaten");
    }

    String token = JwtUtil.generateToken(user.getUsername());
    return ResponseEntity.ok(Map.of("token", token));
}
```
Wenn der Benutzer gültig ist, wird ein **JWT (JSON Web Token)** mit dem Benutzernamen erzeugt und zurückgegeben: {
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
###### 3. Token wird im Browser gespeichert
Das Frontend speichert diesen Token im `localStorage` (oder alternativ im `sessionStorage`), damit der Benutzer bei weiteren Anfragen authentifiziert ist.

###### 4. Weitere geschützte Anfragen mit JWT
Bei weiteren API-Anfragen, die geschützt sind, wird der Token im HTTP-Header `Authorization` mitgeschickt:
``` makefile
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
Bei weiteren API-Anfragen, die geschützt sind, wird der Token im HTTP-Header `Authorization` mitgeschickt

### 🔐 Unterschied zwischen JWT-Authentifizierung und normaler Session:

| **Merkmal**     | **Normale Session**                                                                           | **JWT-Token (Stateless)**                                                              |
| --------------- | --------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------- |
| Speicherung     | Session-Daten werden serverseitig gespeichert (z.B. im Speicher, Datenbank)                   | Token wird komplett clientseitig gespeichert (z.B. localStorage)                       |
| Identifikation  | Session-ID wird als Cookie an Client geschickt                                                | JWT-Token wird als HTTP-Header (Authorization) mitgeschickt                            |
| Zustand (State) | Server hält Zustand (z.B. eingeloggter Benutzer, Rollen)                                      | Server ist zustandslos — der Zustand ist im Token enthalten                            |
| Skalierbarkeit  | Weniger skalierbar, da Server Session-Management benötigt                                     | Sehr gut skalierbar, da keine Sessions gespeichert werden müssen                       |
| Sicherheit      | Sessions sind sicher, da Token nicht clientseitig lesbar, aber anfällig für Session-Hijacking | JWT kann gelesen werden, deshalb wichtig: HTTPS, kurze Ablaufzeit, Signaturprüfung     |
| Ablauf / Logout | Session kann serverseitig invalidiert werden                                                  | Token läuft meist nach Ablauf automatisch ab, Logout muss clientseitig gelöscht werden |