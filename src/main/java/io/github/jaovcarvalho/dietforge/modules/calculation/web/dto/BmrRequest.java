package io.github.jaovcarvalho.dietforge.modules.calculation.web.dto;

import io.github.jaovcarvalho.dietforge.modules.calculation.domain.model.Sex;
import jakarta.validation.constraints.Positive;

public record BmrRequest(
        Sex sex,
        @Positive double weightKg,
        @Positive double heightCm,
        @Positive int age
) {}
