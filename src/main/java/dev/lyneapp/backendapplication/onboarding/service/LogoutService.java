package dev.lyneapp.backendapplication.onboarding.service;


import dev.lyneapp.backendapplication.onboarding.repository.JwtTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(LogoutService.class);
    private final JwtTokenRepository jwtTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LOGGER.info("Logout");
        final String authenticationHeader = request.getHeader("Authorization");
        final String jwtToken;

        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            return;
        }
        jwtToken = authenticationHeader.substring(7);
        var storedToken = jwtTokenRepository.findByToken(jwtToken).orElse(null);

        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            jwtTokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
        LOGGER.info("Logout successful");
    }
}
