package io.github.jaovcarvalho.dietforge.modules.diet.web.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record DietResponse(
        UUID id,
        String name,
        List<DietMealSummary> meals,
        BigDecimal totalKcal,
        BigDecimal totalProtein,
        BigDecimal totalCarbs,
        BigDecimal totalFat
) {}
