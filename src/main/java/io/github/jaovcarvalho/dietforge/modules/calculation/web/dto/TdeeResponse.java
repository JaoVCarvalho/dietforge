package io.github.jaovcarvalho.dietforge.modules.calculation.web.dto;

public record TdeeResponse(
        double bmr,
        double activityFactor,
        double tdee,
        String formula
) {}
