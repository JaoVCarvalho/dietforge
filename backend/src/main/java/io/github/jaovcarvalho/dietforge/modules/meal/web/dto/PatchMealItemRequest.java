package io.github.jaovcarvalho.dietforge.modules.meal.web.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record PatchMealItemRequest(
        @Nullable UUID foodId,
        @Nullable @Positive BigDecimal quantityUpdated) {
}
