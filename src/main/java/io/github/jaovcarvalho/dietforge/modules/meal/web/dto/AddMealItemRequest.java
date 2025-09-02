package io.github.jaovcarvalho.dietforge.modules.meal.web.dto;

import io.github.jaovcarvalho.dietforge.modules.food.domain.model.UnitType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public class AddMealItemRequest {

    @NotNull
    private UUID foodId;
    @NotNull
    private UnitType unitTypeUsed;
    @NotNull @Positive
    private BigDecimal quantityUsed;

    public UUID getFoodId() {
        return foodId;
    }

    public void setFoodId(UUID foodId) {
        this.foodId = foodId;
    }

    public UnitType getUnitTypeUsed() {
        return unitTypeUsed;
    }

    public void setUnitTypeUsed(UnitType unitTypeUsed) {
        this.unitTypeUsed = unitTypeUsed;
    }

    public BigDecimal getQuantityUsed() {
        return quantityUsed;
    }

    public void setQuantityUsed(BigDecimal quantityUsed) {
        this.quantityUsed = quantityUsed;
    }
}
