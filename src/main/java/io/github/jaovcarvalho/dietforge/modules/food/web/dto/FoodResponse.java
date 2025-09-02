package io.github.jaovcarvalho.dietforge.modules.food.web.dto;

import io.github.jaovcarvalho.dietforge.modules.food.domain.model.UnitType;

import java.math.BigDecimal;
import java.util.UUID;

public record FoodResponse(
        UUID id,
        String name,
        UnitType unitType,
        BigDecimal baseAmount,
        BigDecimal kcal,
        BigDecimal protein,
        BigDecimal carbs,
        BigDecimal fat
) {}
