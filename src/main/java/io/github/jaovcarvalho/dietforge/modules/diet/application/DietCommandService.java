package io.github.jaovcarvalho.dietforge.modules.diet.application;

import io.github.jaovcarvalho.dietforge.modules.diet.domain.model.Diet;
import io.github.jaovcarvalho.dietforge.modules.diet.infra.DietRepository;
import io.github.jaovcarvalho.dietforge.modules.meal.domain.model.Meal;
import io.github.jaovcarvalho.dietforge.modules.meal.infra.MealRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DietCommandService {

    private DietRepository dietRepository;
    private MealRepository mealRepository;

    public DietCommandService(DietRepository dietRepository, MealRepository mealRepository) {
        this.dietRepository = dietRepository;
        this.mealRepository = mealRepository;
    }

    @Transactional
    public Diet createDiet(String name){
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name is required");
        if (dietRepository.existsByNameIgnoreCase(name))
            throw new IllegalArgumentException("diet name already exists: " + name);

        Diet diet = new Diet(UUID.randomUUID(), name.strip());
        return dietRepository.save(diet);
    }

    @Transactional
    public Meal createMealInDiet(UUID dietId, String mealName){
        Diet diet = dietRepository.findById(dietId)
                .orElseThrow(() -> new IllegalArgumentException("Diet not found: " + dietId));
        if (mealName == null || mealName.isBlank())
            throw new IllegalArgumentException("mealName is required");

        Meal meal = new Meal(UUID.randomUUID(), mealName.strip());
        diet.addMeal(meal);
        dietRepository.save(diet);
        return meal;
    }

    @Transactional
    public void removeMealInDiet(UUID dietId, UUID mealId){
        Diet diet = dietRepository.findById(dietId)
                .orElseThrow(() -> new IllegalArgumentException("Diet not found: " + dietId));
        diet.getMeals().removeIf(m -> m.getId().equals(mealId));
        dietRepository.save(diet);
    }


}
