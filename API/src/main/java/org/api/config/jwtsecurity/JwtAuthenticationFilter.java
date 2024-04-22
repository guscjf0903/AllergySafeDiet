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
import org.api.exception.JwtValidationException;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
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
            String accessToken = getJwtFromRequest(httpRequest);
            if (accessToken != null && jwtConfig.validateToken(accessToken)) {
                authenticate(accessToken);
            }else {
                String refreshToken = httpRequest.getHeader("Refresh-Token");
                if (refreshToken != null && jwtConfig.validateToken(refreshToken)) {
                    String newAccessToken = jwtConfig.generateAccessToken(jwtConfig.getUserIdFromJwtToken(refreshToken));
                    httpResponse.setHeader("New-Access-Token", newAccessToken);
                    authenticate(newAccessToken);
                }
            }
        } catch (JwtValidationException e) {
            log.error("JWT Validation Exception: {}", e.getMessage());
        }
        chain.doFilter(request, response);

    }

    private void authenticate(String jwt) {
        Long userId = jwtConfig.getUserIdFromJwtToken(jwt);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
