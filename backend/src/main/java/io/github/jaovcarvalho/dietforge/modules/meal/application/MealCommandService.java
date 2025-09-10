package io.github.jaovcarvalho.dietforge.modules.meal.application;

import io.github.jaovcarvalho.dietforge.modules.food.domain.model.Food;
import io.github.jaovcarvalho.dietforge.modules.food.domain.model.UnitType;
import io.github.jaovcarvalho.dietforge.modules.food.infra.FoodRepository;
import io.github.jaovcarvalho.dietforge.modules.meal.domain.model.Meal;
import io.github.jaovcarvalho.dietforge.modules.meal.domain.model.MealItem;
import io.github.jaovcarvalho.dietforge.modules.meal.infra.MealItemRepository;
import io.github.jaovcarvalho.dietforge.modules.meal.infra.MealRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
public class MealCommandService {

    private static final int SCALE = 2;

    private final MealRepository mealRepository;
    private final MealItemRepository mealItemRepository;
    private final FoodRepository foodRepository;

    public MealCommandService(MealRepository mealRepository,
                              MealItemRepository mealItemRepository,
                              FoodRepository foodRepository) {
        this.mealRepository = mealRepository;
        this.mealItemRepository = mealItemRepository;
        this.foodRepository = foodRepository;
    }

    @Transactional
    public Meal createMeal(String name){
        if(name.isBlank() || name == null){
            throw new IllegalArgumentException("name is required");
        }
        Meal meal = new Meal(UUID.randomUUID(), name.trim());
        return mealRepository.save(meal);
    }

    @Transactional
    public MealItem addItem(UUID mealID,
                            UUID foodId,
                            UnitType unitTypeUsed,
                            BigDecimal quantityUsed){

        Meal meal = mealRepository.findById(mealID)
                .orElseThrow(() -> new IllegalArgumentException("meal not found: " + mealID));

        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new IllegalArgumentException("food not found: " + foodId));

        if(quantityUsed ==  null || quantityUsed.signum() <= 0) {
            throw new IllegalArgumentException("quantityUsed must be > 0");
        }

        if(unitTypeUsed != food.getUnitType()){
            throw new IllegalArgumentException("unitTypeUsed must match food.unitType");
        }

        BigDecimal factor = quantityUsed.divide(food.getBaseAmount(),
                SCALE + 2,
                RoundingMode.HALF_UP);

        BigDecimal kcal = food.getKcal()
                .multiply(factor)
                .setScale(SCALE, RoundingMode.HALF_UP);
        BigDecimal protein = food.getProtein()
                .multiply(factor)
                .setScale(SCALE, RoundingMode.HALF_UP);
        BigDecimal carbs = food.getCarbs()
                .multiply(factor)
                .setScale(SCALE, RoundingMode.HALF_UP);
        BigDecimal fat = food.getFat()
                .multiply(factor)
                .setScale(SCALE, RoundingMode.HALF_UP);

        MealItem mealItem = new MealItem(UUID.randomUUID(),
                food,
                unitTypeUsed,
                quantityUsed,
                kcal,
                protein,
                carbs,
                fat);

        meal.addItem(mealItem);
        mealRepository.save(meal);

        return mealItem;
    }

    @Transactional
    public void removeItem(UUID mealId, UUID mealItemId){
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new IllegalArgumentException("Meal not found: " + mealId));

        meal.getItems().removeIf(it -> it.getId().equals(mealItemId));
        mealRepository.save(meal);
    }
}
