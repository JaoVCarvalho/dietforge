package io.github.jaovcarvalho.dietforge.modules.meal.web.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateMealRequest {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
