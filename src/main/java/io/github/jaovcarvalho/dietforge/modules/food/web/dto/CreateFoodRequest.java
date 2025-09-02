package io.github.jaovcarvalho.dietforge.modules.food.web.dto;

import io.github.jaovcarvalho.dietforge.modules.food.domain.model.UnitType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class CreateFoodRequest {

    @NotBlank
    private String name;
    @NotNull
    private UnitType unitType;
    @NotNull @Positive
    private BigDecimal baseAmount;
    @NotNull @DecimalMin("0.00")
    private BigDecimal kcal;
    @NotNull @DecimalMin("0.00")
    private BigDecimal protein;
    @NotNull @DecimalMin("0.00")
    private BigDecimal carbs;
    @NotNull @DecimalMin("0.00")
    private BigDecimal fat;

    public String getName() {
        return name;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public BigDecimal getKcal() {
        return kcal;
    }

    public BigDecimal getProtein() {
        return protein;
    }

    public BigDecimal getCarbs() {
        return carbs;
    }

    public BigDecimal getFat() {
        return fat;
    }
}
