package org.core.response;

import java.util.List;

public record AllergyResponse (
        List<String> allergies
) {
    public static AllergyResponse toResponse(List<String> allergies) {
        return new AllergyResponse(allergies);
    }
}
