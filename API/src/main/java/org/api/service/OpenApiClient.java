package org.api.service;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.api.config.OpenApiProperties;
import org.api.enumtype.ApiConstants;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class OpenApiClient {
    private final RestTemplate restTemplate;
    private final OpenApiProperties openApiProperties;

    public String callRecipeApi(String pathSegment, String queryParamKey, Object queryParamValue) {
        URI requestUrl = UriComponentsBuilder.fromHttpUrl(openApiProperties.getBaseUrl())
                .pathSegment(openApiProperties.getServiceKey(), ApiConstants.JSON_FORMAT, pathSegment,
                        ApiConstants.DEFAULT_PAGE, ApiConstants.DEFAULT_SIZE)
                .queryParam(queryParamKey, queryParamValue)
                .encode()
                .build()
                .toUri();

        return restTemplate.getForObject(requestUrl, String.class);
    }
}
