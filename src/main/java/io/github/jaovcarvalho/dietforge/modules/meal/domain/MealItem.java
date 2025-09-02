package io.github.jaovcarvalho.dietforge.modules.meal.domain;

import io.github.jaovcarvalho.dietforge.modules.food.domain.model.Food;
import io.github.jaovcarvalho.dietforge.modules.food.domain.model.UnitType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "meal_items")
public class MealItem {

    @Id
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id",
            nullable = false,
            columnDefinition = "BINARY(16)")
    private UUID id;

    // optional = false → indica que o relacionamento é obrigatório
    // Nesse caso, para um meal_item existir ele necessita de uma meal
    @ManyToOne(fetch = FetchType.LAZY,
            optional = false)
    @JoinColumn(name = "meal_id",
            nullable = false)
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY,
            optional = false)
    @JoinColumn(name = "food_id",
            nullable = false)
    private Food food;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_type_used", nullable = false)
    private UnitType unitType;

    @Column(name = "quantity_used",
            nullable = false,
            precision = 10,
            scale = 2)
    private BigDecimal quantityUsed;

    @Column(name = "kcal_resolved",
            nullable = false,
            precision = 10,
            scale = 2)
    private BigDecimal kcalResolved;

    @Column(name = "protein_resolved",
            nullable = false,
            precision = 10,
            scale = 2)
    private BigDecimal proteinResolved;

    @Column(name = "carbs_resolved",
            nullable = false,
            precision = 10,
            scale = 2)
    private BigDecimal carbsResolved;

    @Column(name = "fat_resolved",
            nullable = false,
            precision = 10,
            scale = 2)
    private BigDecimal fatResolved;

    @CreationTimestamp
    @Column(name = "created_at",
            nullable = false,
            updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",
            nullable = false)
    private Instant updatedAt;

    protected MealItem() {
    }

    public MealItem(UUID id,
                    Food food,
                    UnitType unitType,
                    BigDecimal quantityUsed,
                    BigDecimal kcalResolved,
                    BigDecimal proteinResolved,
                    BigDecimal carbsResolved,
                    BigDecimal fatResolved) {
        this.id = id;
        this.food = food;
        this.unitType = unitType;
        this.quantityUsed = quantityUsed;
        this.kcalResolved = kcalResolved;
        this.proteinResolved = proteinResolved;
        this.carbsResolved = carbsResolved;
        this.fatResolved = fatResolved;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public UUID getId() {
        return id;
    }

    public Meal getMeal() {
        return meal;
    }

    public Food getFood() {
        return food;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public BigDecimal getQuantityUsed() {
        return quantityUsed;
    }

    public BigDecimal getKcalResolved() {
        return kcalResolved;
    }

    public BigDecimal getProteinResolved() {
        return proteinResolved;
    }

    public BigDecimal getCarbsResolved() {
        return carbsResolved;
    }

    public BigDecimal getFatResolved() {
        return fatResolved;
    }
}
