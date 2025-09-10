package io.github.jaovcarvalho.dietforge.modules.calculation.web.dto;

import io.github.jaovcarvalho.dietforge.modules.calculation.domain.model.ActivityLevel;
import io.github.jaovcarvalho.dietforge.modules.calculation.domain.model.Sex;
import jakarta.validation.constraints.Positive;

public record TdeeRequest(
        Sex sex,
        @Positive double weightKg,
        @Positive double heightCm,
        @Positive int age,
        ActivityLevel activityLevel
) {}
