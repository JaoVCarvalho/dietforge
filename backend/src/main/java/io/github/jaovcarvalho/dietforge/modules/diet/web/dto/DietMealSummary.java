package io.github.jaovcarvalho.dietforge.modules.diet.web.dto;

import java.util.List;
import java.util.UUID;

public record DietMealSummary(
        UUID id,
        String name,
        List<DietMealItemSummary> items
) {}
