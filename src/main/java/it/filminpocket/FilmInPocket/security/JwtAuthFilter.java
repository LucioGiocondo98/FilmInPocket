package it.filminpocket.FilmInPocket.security;

import it.filminpocket.FilmInPocket.entities.User;
import it.filminpocket.FilmInPocket.exceptions.UnauthorizedException;
import it.filminpocket.FilmInPocket.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        /**
         *  Controlla se l'header Authorization è presente e formattato correttamente
        */
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        /**
         *  Estrai il token
        */
        String token = authHeader.substring(7);

        try {
            /**
             *  Verifica il token e estrai l'ID utente
             */
            jwtTool.verifyToken(token);
            int userId = Integer.parseInt(jwtTool.getSubject(token));

            /**
             *  Cerca l'utente nel DB e autenticalo in Spring
            */
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UnauthorizedException("Token valido ma utente non trovato nel database"));

            /**
             * Crea l'oggetto Authentication con i ruoli (authorities)
            */
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            /**
             * Imposta l'autenticazione nel contesto di sicurezza
            */
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            logger.error("Errore durante la validazione del token JWT: {}", e.getMessage());
        }

        // 7. Prosegui con la catena dei filtri
        filterChain.doFilter(request, response);
    }

    // Usiamo il metodo più robusto per escludere i percorsi
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Questo filtro non deve essere attivo per le rotte di login e registrazione
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}

