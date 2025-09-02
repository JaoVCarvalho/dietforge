package io.github.jaovcarvalho.dietforge.modules.food.application;

import io.github.jaovcarvalho.dietforge.modules.food.domain.model.Food;
import io.github.jaovcarvalho.dietforge.modules.food.domain.model.UnitType;
import io.github.jaovcarvalho.dietforge.modules.food.infra.FoodRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class FoodCommandService {

    public final FoodRepository foodRepository;

    public FoodCommandService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public Food create(
            String name,
            UnitType unitType,
            BigDecimal baseAmount,
            BigDecimal kcal,
            BigDecimal protein,
            BigDecimal carbs,
            BigDecimal fat){

        if(name == null || name.isBlank()) throw new IllegalArgumentException("name is required");
        if(baseAmount == null || baseAmount.signum() <= 0) throw new IllegalArgumentException("baseAmount must be > 0");
        if(kcal == null || protein == null || carbs == null || fat == null) throw new IllegalArgumentException("Nutrition fields are required");
        if(foodRepository.existsByNameIgnoreCase(name)) throw new IllegalArgumentException("food name already exists: " + name);

        Food food = new Food(UUID.randomUUID(), name, unitType, baseAmount, kcal, protein, carbs, fat);
        return foodRepository.save(food);
    }
}
