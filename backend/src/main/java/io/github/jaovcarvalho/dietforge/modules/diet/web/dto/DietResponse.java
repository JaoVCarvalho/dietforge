package io.github.jaovcarvalho.dietforge.modules.diet.web.dto;

import io.github.jaovcarvalho.dietforge.modules.diet.application.Totals;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record DietResponse(
        UUID id,
        String name,
        List<DietMealSummary> meals,
        Totals totals
) {
}
