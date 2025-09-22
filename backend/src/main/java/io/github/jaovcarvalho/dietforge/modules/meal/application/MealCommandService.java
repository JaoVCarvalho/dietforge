package io.github.jaovcarvalho.dietforge.modules.meal.application;

import io.github.jaovcarvalho.dietforge.modules.food.domain.model.Food;
import io.github.jaovcarvalho.dietforge.modules.food.infra.FoodRepository;
import io.github.jaovcarvalho.dietforge.modules.meal.domain.model.Meal;
import io.github.jaovcarvalho.dietforge.modules.meal.domain.model.MealItem;
import io.github.jaovcarvalho.dietforge.modules.meal.infra.MealItemRepository;
import io.github.jaovcarvalho.dietforge.modules.meal.infra.MealRepository;
import io.github.jaovcarvalho.dietforge.modules.meal.web.dto.AddMealItemRequest;
import io.github.jaovcarvalho.dietforge.modules.meal.web.dto.PatchMealItemRequest;
import io.github.jaovcarvalho.dietforge.modules.meal.web.dto.UpdateMealRequest;
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
    public Meal update(UpdateMealRequest request) {
        Meal meal = mealRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Meal %s not found".formatted(request.id())));

        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("Meal name is required");
        }

        if (!meal.getName().equalsIgnoreCase(request.name())
                && mealRepository.existsByDietIdAndNameIgnoreCase(meal.getDiet().getId(), request.name())) {
            throw new IllegalArgumentException("A meal with the name %s already exists in this diet".formatted(request.name()));
        }

        meal.setName(request.name());
        return mealRepository.save(meal);
    }

    @Transactional
    public MealItem addItem(UUID mealID, AddMealItemRequest request) {

        Meal meal = mealRepository.findById(mealID)
                .orElseThrow(() -> new IllegalArgumentException("Meal %s not found".formatted(mealID)));

        Food food = foodRepository.findById(request.foodId())
                .orElseThrow(() -> new IllegalArgumentException("Food %s not found".formatted(request.foodId())));

        if (request.quantityUsed() == null || request.quantityUsed().signum() <= 0) {
            throw new IllegalArgumentException("quantityUsed must be > 0");
        }

        Macro macros = calculateResolvedMacros(food, request.quantityUsed());

        MealItem mealItem = new MealItem(UUID.randomUUID(),
                food,
                food.getUnitType(),
                request.quantityUsed(),
                macros.kcal,
                macros.protein,
                macros.carbs,
                macros.fat);

        meal.addItem(mealItem);
        mealRepository.save(meal);

        return mealItem;
    }

    @Transactional
    public MealItem patchItem(UUID mealItemId, PatchMealItemRequest request) {

        MealItem mealItem = mealItemRepository.findById(mealItemId)
                .orElseThrow(() -> new IllegalArgumentException("Item %s not found".formatted(mealItemId)));

        if (request.foodId() != null) {
            Food food = foodRepository.findById(request.foodId())
                    .orElseThrow(() -> new IllegalArgumentException("Food %s not found".formatted(request.foodId())));

            mealItem.setFood(food);
            mealItem.setUnitTypeUsed(food.getUnitType());
        }

        if (request.quantityUpdated() != null) {
            if (request.quantityUpdated().signum() <= 0) {
                throw new IllegalArgumentException("Quantity updated must be > 0");
            }

            mealItem.setQuantityUsed(request.quantityUpdated());

            Macro macros = calculateResolvedMacros(mealItem.getFood(), request.quantityUpdated());
            mealItem.setKcalResolved(macros.kcal);
            mealItem.setProteinResolved(macros.protein);
            mealItem.setCarbsResolved(macros.carbs);
            mealItem.setFatResolved(macros.fat);
        }

        return mealItemRepository.save(mealItem);
    }

    @Transactional
    public void removeItem(UUID mealId, UUID mealItemId) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new IllegalArgumentException("Meal not found: " + mealId));

        meal.getItems().removeIf(it -> it.getId().equals(mealItemId));
        mealRepository.save(meal);
    }

    private record Macro(
            BigDecimal kcal,
            BigDecimal protein,
            BigDecimal carbs,
            BigDecimal fat) {
    }

    private Macro calculateResolvedMacros(Food food, BigDecimal quantityUsed) {

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

        return new Macro(kcal, protein, carbs, fat);
    }
}




