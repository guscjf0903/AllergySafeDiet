package org.api.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MenuSuggestionService {
    private final RestTemplate restTemplate;

    public List<String> getMenuSuggestions(String menuName) {

        return
    }

}
