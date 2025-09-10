package io.github.jaovcarvalho.dietforge.modules.meal.web.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record MealResponse(
   UUID id,
   String name,
   List<MealItemResponse> items,
   BigDecimal totalKcal,
   BigDecimal totalProtein,
   BigDecimal totalCarbs,
   BigDecimal totalFat
) {}
