package io.github.jaovcarvalho.dietforge.modules.food.application;

import io.github.jaovcarvalho.dietforge.modules.food.domain.model.Food;
import io.github.jaovcarvalho.dietforge.modules.food.domain.model.UnitType;
import io.github.jaovcarvalho.dietforge.modules.food.infra.FoodRepository;
import io.github.jaovcarvalho.dietforge.modules.food.web.dto.PatchFoodRequest;
import io.github.jaovcarvalho.dietforge.modules.food.web.dto.UpdateFoodRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class FoodCommandService {

    public final FoodRepository foodRepository;

    public FoodCommandService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Transactional
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

    @Transactional
    public Food update(UpdateFoodRequest request){
        Food food = foodRepository.findById(request.id())
                .orElseThrow(()-> new IllegalArgumentException("Food %s not found".formatted(request.id())));

        if (!food.getName().equalsIgnoreCase(request.name())
                && foodRepository.existsByNameIgnoreCase(request.name())){
            throw new IllegalArgumentException("Food name already exists: " + request.name());
        }

        food.setName(request.name());
        food.setUnitType(request.unitType());
        food.setBaseAmount(request.baseAmount());
        food.setKcal(request.kcal());
        food.setProtein(request.protein());
        food.setCarbs(request.carbs());
        food.setFat(request.fat());

        return foodRepository.save(food);
    }

    @Transactional
    public Food patch(UUID id, PatchFoodRequest request){

        Food food = foodRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Food %s not found".formatted(id)));

        if (request.name() != null){
           String newName = request.name().strip();
           if (newName.isBlank()) throw new IllegalArgumentException("name cannot be blank");
           if (!food.getName().equalsIgnoreCase(request.name())
                   && foodRepository.existsByNameIgnoreCase(request.name())){
               throw new IllegalArgumentException("Food name already exists: " + request.name());
           }
           food.setName(request.name());
        }

        if (request.unitType() != null) food.setUnitType(request.unitType());
        if (request.baseAmount() != null) {
            if (request.baseAmount().signum() <= 0) throw new IllegalArgumentException("baseAmount must be > 0");
            food.setBaseAmount(request.baseAmount());
        }

        if (request.kcal() != null) food.setKcal(request.kcal());
        if (request.protein() != null) food.setProtein(request.protein());
        if (request.carbs() != null) food.setCarbs(request.carbs());
        if (request.fat() != null) food.setFat(request.fat());

        return foodRepository.save(food);
    }

    @Transactional
    public void delete (UUID id) {
        Food food = foodRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Food %s not found".formatted(id)));
        foodRepository.delete(food);
    }
}
