package org.api.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "openapi")
@RequiredArgsConstructor
@Getter
@Setter
public class OpenApiProperties {
    private String baseUrl;
    private String serviceKey;
    private String recipeInfoPath;
    private String recipeDetailsPath;
}
