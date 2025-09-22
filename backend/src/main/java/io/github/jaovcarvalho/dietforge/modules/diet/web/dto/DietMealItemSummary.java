package io.github.jaovcarvalho.dietforge.modules.diet.web.dto;

import io.github.jaovcarvalho.dietforge.modules.diet.application.Totals;
import io.github.jaovcarvalho.dietforge.modules.food.domain.model.UnitType;

import java.math.BigDecimal;
import java.util.UUID;

public record DietMealItemSummary(
        UUID id,
        UUID foodId,
        String foodName,
        UnitType unitTypeUsed,
        BigDecimal quantityUsed,
        Totals totals
) {}
