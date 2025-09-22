package io.github.jaovcarvalho.dietforge.modules.meal.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateMealRequest(
        @NotNull UUID id,
        @NotBlank String name
) {
}
