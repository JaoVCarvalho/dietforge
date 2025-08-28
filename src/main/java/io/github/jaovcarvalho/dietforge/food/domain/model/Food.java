package io.github.jaovcarvalho.dietforge.food.domain.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "foods")
public class Food {

    @Id
    // Mapeia UUID Java -> Binary JDBC
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name", nullable = false, length = 120, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_type", nullable = false)
    private UnitType unitType;

    // precision → número total de dígitos
    // scale → números à direita da vírgula
    @Column(name = "base_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseAmount;

    @Column(name = "kcal", nullable = false, precision = 10, scale = 2)
    private BigDecimal kcal;

    @Column(name = "protein_g", nullable = false, precision = 10, scale = 2)
    private BigDecimal protein;

    @Column(name = "carbs_g", nullable = false, precision = 10, scale = 2)
    private BigDecimal carbs;

    @Column(name = "fat_g", nullable = false, precision = 10, scale = 2)
    private BigDecimal fat;

    @CreationTimestamp
    // updatable → Garantir que o timestamp não sejam alteradas
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // Construtor JPA
    protected Food() {
    }

    public Food(UUID id, String name, UnitType unitType, BigDecimal baseAmount, BigDecimal kcal, BigDecimal protein, BigDecimal carbs, BigDecimal fat) {
        this.id = id;
        this.name = name;
        this.unitType = unitType;
        this.baseAmount = baseAmount;
        this.kcal = kcal;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public BigDecimal getKcal() {
        return kcal;
    }

    public void setKcal(BigDecimal kcal) {
        this.kcal = kcal;
    }

    public BigDecimal getProtein() {
        return protein;
    }

    public void setProtein(BigDecimal protein) {
        this.protein = protein;
    }

    public BigDecimal getCarbs() {
        return carbs;
    }

    public void setCarbs(BigDecimal carbs) {
        this.carbs = carbs;
    }

    public BigDecimal getFat() {
        return fat;
    }

    public void setFat(BigDecimal fat) {
        this.fat = fat;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
