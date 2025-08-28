package io.github.jaovcarvalho.dietforge.food.infra;

import io.github.jaovcarvalho.dietforge.food.domain.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FoodRepository extends JpaRepository<Food, UUID> {

    boolean existsByNameIgnoreCase(String name);
    Page<Food> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
