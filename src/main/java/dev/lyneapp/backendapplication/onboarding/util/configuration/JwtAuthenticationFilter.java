package dev.lyneapp.backendapplication.onboarding.util.configuration;


import dev.lyneapp.backendapplication.onboarding.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// TODO Implement refresh token logic - ask users to revalidate access once the current jwt token is expired (I'll use 3 months) - https://youtu.be/EsVybSJr7zU
/**
 *  -  Guide: There are some steps to login / revoke access to an api:

        When you do log in, send 2 tokens (Access token, Refresh token) in response to the client.
        The access token will have less expiry time and Refresh will have long expiry time.
        The client (Front end) will store refresh token in his local storage and access token in cookies.
        The client will use an access token for calling APIs. But when it expires,
        pick the refresh token from local storage and call auth server API to get the new token.
        Your auth server will have an API exposed which will accept refresh token and checks for its validity and return a new access token.
        Once the refresh token is expired, the User will be logged out.
        JSON Web Tokens are a good way of securely transmitting information between parties.
        Because JWTs can be signed—for example, using public/private key pairs—you can be sure the senders are who they say they are.
        Additionally, as the signature is calculated using the header and the payload, you can also verify that the content hasn't been tampered with.
 */
// TODO implement elevated access (Admin logic) - https://youtu.be/mq5oUXcAXL4

// TODO implement the openAPI config - https://www.youtube.com/watch?v=2o_3hjUPAfQ

// TODO Implement Containerization and CI/CD pipeline - https://youtu.be/a5qkPEod9ng & https://youtu.be/LNL0h66FXu0

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authenticationHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userPhoneNumber;

        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwtToken = authenticationHeader.substring(7);
        userPhoneNumber = jwtService.extractUserPhoneNumber(jwtToken);

        if (userPhoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userPhoneNumber);

            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
