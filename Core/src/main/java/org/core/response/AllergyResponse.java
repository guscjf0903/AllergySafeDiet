package org.core.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.core.request.IngredientsRequest;

public record AllergyResponse (
        List<String> allergies
) {
    public static AllergyResponse toResponse(List<String> allergies) {
        return new AllergyResponse(allergies);
    }
}
