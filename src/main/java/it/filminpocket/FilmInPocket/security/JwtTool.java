package it.filminpocket.FilmInPocket.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.filminpocket.FilmInPocket.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTool {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.duration}")
    private long duration;



    /**
     * Crea un nuovo token JWT per un utente.
     */
    public String createToken(User user) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + duration))
                .subject(String.valueOf(user.getId()))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }


    /**
     * Verifica la validità di un token (firma e scadenza).
     * Lancia un'eccezione se il token non è valido.
     */
    public void verifyToken(String token) {
        Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parse(token);
    }


    /**
     * Estrae l'ID dell'utente (il subject) dal token, dopo averlo validato.
     */
    public String getSubject(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}