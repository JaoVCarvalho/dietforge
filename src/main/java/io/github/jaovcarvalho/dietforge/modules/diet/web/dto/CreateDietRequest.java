package io.github.jaovcarvalho.dietforge.modules.diet.web.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateDietRequest {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
