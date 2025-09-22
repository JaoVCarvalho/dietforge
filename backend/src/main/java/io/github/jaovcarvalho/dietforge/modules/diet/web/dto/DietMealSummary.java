package io.github.jaovcarvalho.dietforge.modules.diet.web.dto;

import io.github.jaovcarvalho.dietforge.modules.diet.application.Totals;

import java.util.List;
import java.util.UUID;

public record DietMealSummary(
        UUID id,
        String name,
        List<DietMealItemSummary> items,
        Totals totals
) {}
