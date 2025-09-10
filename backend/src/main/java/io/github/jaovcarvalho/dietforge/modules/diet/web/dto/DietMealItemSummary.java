package io.github.jaovcarvalho.dietforge.modules.diet.web.dto;

import io.github.jaovcarvalho.dietforge.modules.food.domain.model.UnitType;

import java.math.BigDecimal;
import java.util.UUID;

public record DietMealItemSummary(
        UUID id,
        UUID foodId,
        String foodName,
        UnitType unitTypeUsed,
        BigDecimal quantityUsed,
        BigDecimal kcal,
        BigDecimal protein,
        BigDecimal carbs,
        BigDecimal fat
) {}
