package org.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.entity.AllergyEntity;
import org.api.entity.LoginEntity;
import org.api.entity.UserEntity;
import org.api.repository.AllergicRepository;
import org.api.repository.UserRepository;
import org.core.request.AllergyRequest;
import org.core.response.AllergyResponse;
import org.h2.engine.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AllergyService {
    private final AllergicRepository allergicRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<AllergyResponse> getAllergyData(UserEntity userEntity) {
        Optional<UserEntity> user = userRepository.findByUserId(userEntity.getUserId());
        List<String> allergiesList = new ArrayList<>();

        if (user.isEmpty()) {
            return Optional.empty();
        } else {
            UserEntity orgUser = user.get();
            List<AllergyEntity> allergyEntities = orgUser.getAllergyEntities();
            for (AllergyEntity allergy : allergyEntities) {
                allergiesList.add(allergy.getAllergen());
            }
            AllergyResponse allergyResponse = AllergyResponse.toResponse(allergiesList);
            return Optional.of(allergyResponse);
        }
    }


    @Transactional
    public void saveAllergyData(AllergyRequest allergyRequest, UserEntity userEntity) {
        for (String allergen : allergyRequest.allergy()) {
            AllergyEntity allergyEntity = AllergyEntity.of(userEntity, allergen);
            allergicRepository.save(allergyEntity);
        }
    }

    @Transactional
    public void putAllergyData(AllergyRequest allergyRequest, UserEntity userEntity) {
        allergicRepository.deleteByUserUserId(userEntity.getUserId());
        saveAllergyData(allergyRequest, userEntity);
    }

}
