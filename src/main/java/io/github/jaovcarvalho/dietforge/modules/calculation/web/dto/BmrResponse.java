package io.github.jaovcarvalho.dietforge.modules.calculation.web.dto;

public record BmrResponse(
        double bmr,
        String formula
) {}
