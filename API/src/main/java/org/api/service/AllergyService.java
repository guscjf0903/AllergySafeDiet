package org.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.entity.AllergyEntity;
import org.api.entity.LoginEntity;
import org.api.repository.AllergicRepository;
import org.core.request.AllergyRequest;
import org.core.response.AllergyResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AllergyService {
    private final LoginService loginService;
    private final AllergicRepository allergicRepository;
    @Transactional(readOnly = true)
    public Optional<AllergyResponse> getAllergyData(String authorizationHeader) {
        LoginEntity loginEntity = getUserIdFromHeader(authorizationHeader);
        List<String> allergiesList = new ArrayList<>();
        List<AllergyEntity> allergyEntities = loginEntity.getUser().getAllergyEntities();

        if(allergyEntities.isEmpty()) {
            return Optional.empty();
        } else {
            for(AllergyEntity allergy : allergyEntities) {
                allergiesList.add(allergy.getAllergen());
            }
            AllergyResponse allergyResponse = AllergyResponse.toResponse(allergiesList);
            return Optional.of(allergyResponse);
        }
    }

    @Transactional
    public void saveAllergyData(AllergyRequest allergyRequest, String authorizationHeader) {
        LoginEntity loginEntity = getUserIdFromHeader(authorizationHeader);

        for(String allergen : allergyRequest.allergy()) {
            AllergyEntity allergyEntity = AllergyEntity.of(loginEntity.getUser(), allergen);
            allergicRepository.save(allergyEntity);
        }
    }

    @Transactional
    public void putAllergyData(AllergyRequest allergyRequest, String authorizationHeader) {
        LoginEntity loginEntity = getUserIdFromHeader(authorizationHeader);

        allergicRepository.deleteByUserUserId(loginEntity.getUser().getUserId());
        saveAllergyData(allergyRequest, authorizationHeader);
    }

    private LoginEntity getUserIdFromHeader(String authorizationHeader) {
        return loginService.validateLoginId(authorizationHeader);
    }
}
