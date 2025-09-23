package io.github.jaovcarvalho.dietforge.modules.diet.web.dto;

import io.github.jaovcarvalho.dietforge.modules.diet.application.Totals;

import java.util.UUID;

public record DietSummaryResponse(
        UUID id,
        String name,
        Totals totals
) {
}
