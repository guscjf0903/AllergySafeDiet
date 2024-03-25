package org.core.request;

import java.util.List;

public record AllergyRequest(
        List<String> allergy
) { }
