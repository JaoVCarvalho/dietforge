package io.github.jaovcarvalho.dietforge.modules.meal.domain;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "meals")
public class Meal {

    @Id
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id",
            nullable = false,
            columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name",
            nullable = false,
            length = 120)
    private String name;

    // orphanRemoval = true → Ao remover um item da lista, o Hibernate remove esse registro do banco
    @OneToMany(mappedBy = "meal",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<MealItem> items = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at",
            nullable = false,
            updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",
            nullable = false)
    private Instant updatedAt;

    protected Meal() {
    }

    public Meal(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<MealItem> getItems() {
        return items;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addItem(MealItem item) {
        items.add(item);
        item.setMeal(this);
    }

    public void removeItem(MealItem item) {
        items.remove(item);
        item. setMeal(null);
    }
}
