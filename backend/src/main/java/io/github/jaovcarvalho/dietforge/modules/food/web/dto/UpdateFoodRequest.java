package io.github.jaovcarvalho.dietforge.modules.food.web.dto;

import io.github.jaovcarvalho.dietforge.modules.food.domain.model.UnitType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateFoodRequest(
        @NotNull UUID id,
        @NotBlank String name,
        @NotNull UnitType unitType,
        @NotNull @Positive BigDecimal baseAmount,
        @NotNull @DecimalMin("0.00") BigDecimal kcal,
        @NotNull @DecimalMin("0.00") BigDecimal protein,
        @NotNull @DecimalMin("0.00") BigDecimal carbs,
        @NotNull @DecimalMin("0.00") BigDecimal fat
) {}
