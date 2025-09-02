package io.github.jaovcarvalho.dietforge.modules.meal.infra;

import io.github.jaovcarvalho.dietforge.modules.meal.domain.MealItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MealItemRepository extends JpaRepository<MealItem, UUID> { }
