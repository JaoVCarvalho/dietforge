package io.github.jaovcarvalho.dietforge.modules.food.web.dto;

import io.github.jaovcarvalho.dietforge.modules.food.domain.model.UnitType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PatchFoodRequest(
        @Nullable String name,
        @Nullable UnitType unitType,
        @Nullable @Positive BigDecimal baseAmount,
        @Nullable @DecimalMin("0.00") BigDecimal kcal,
        @Nullable @DecimalMin("0.00") BigDecimal protein,
        @Nullable @DecimalMin("0.00") BigDecimal carbs,
        @Nullable @DecimalMin("0.00") BigDecimal fat
) {}
