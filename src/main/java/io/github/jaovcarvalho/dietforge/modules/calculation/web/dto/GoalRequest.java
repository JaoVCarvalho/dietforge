package io.github.jaovcarvalho.dietforge.modules.calculation.web.dto;

import io.github.jaovcarvalho.dietforge.modules.calculation.domain.model.GoalType;
import jakarta.validation.constraints.Positive;

public record GoalRequest(
        @Positive double tdee,
        GoalType goalType,
        Double delta
) {}
