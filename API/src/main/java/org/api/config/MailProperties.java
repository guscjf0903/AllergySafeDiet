package org.api.config;

import java.util.Properties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@RequiredArgsConstructor
@Getter
@Setter
public class MailProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private Properties properties;
}
