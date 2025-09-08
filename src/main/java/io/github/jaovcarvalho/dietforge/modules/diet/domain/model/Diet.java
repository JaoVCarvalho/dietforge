package io.github.jaovcarvalho.dietforge.modules.diet.domain.model;

import io.github.jaovcarvalho.dietforge.modules.meal.domain.model.Meal;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "diets")
public class Diet {

    @Id
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id",
            nullable = false,
            columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name",
            nullable = false,
            length = 120,
            unique = true)
    private String name;

    @OneToMany(mappedBy = "diet",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Meal> meals = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at",
            nullable = false,
            updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",
            nullable = false)
    private Instant updateAt;

    protected Diet() {
    }

    public Diet(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdateAt() {
        return updateAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMeal(Meal meal){
        meals.add(meal);
        meal.setDiet(this);
    }

    public void removeMeal(Meal meal){
        meals.remove(meal);
        meal.setDiet(null);
    }
}
