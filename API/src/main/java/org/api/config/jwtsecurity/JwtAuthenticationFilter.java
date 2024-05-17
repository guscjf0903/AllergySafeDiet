package org.api.config.jwtsecurity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtConfig jwtConfig;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            String accessToken = extractJwtFromRequest(httpRequest);

            if (accessToken != null && jwtConfig.validateToken(accessToken)) {
                setupAuthentication(accessToken);
            } else {
                refreshToken(httpRequest, httpResponse);
            }
        } catch (Exception e) {
            handleJwtException(e, request);
        }
        chain.doFilter(request, response);
    }

    private void refreshToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String refreshToken = httpRequest.getHeader("refreshToken");
        if (refreshToken != null && jwtConfig.validateToken(refreshToken)) {
            String newAccessToken = jwtConfig.generateAccessToken(jwtConfig.getUserIdFromJwtToken(refreshToken));
            httpResponse.setHeader("New-Access-Token", newAccessToken);
            setupAuthentication(newAccessToken);
        }
    }

    private void handleJwtException(Exception e, ServletRequest request) {
        log.error("JWT Validation Exception: {}", e.getMessage());
        request.setAttribute("exception", e);
    }

    private void setupAuthentication(String jwt) {
        Long userId = jwtConfig.getUserIdFromJwtToken(jwt);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
