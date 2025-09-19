package io.github.jaovcarvalho.dietforge.modules.diet.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateDietRequest(
        @NotNull UUID id,
        @NotBlank String name) {}
