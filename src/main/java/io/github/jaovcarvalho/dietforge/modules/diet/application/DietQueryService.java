package io.github.jaovcarvalho.dietforge.modules.diet.application;

import io.github.jaovcarvalho.dietforge.modules.diet.domain.model.Diet;
import io.github.jaovcarvalho.dietforge.modules.diet.infra.DietRepository;
import io.github.jaovcarvalho.dietforge.modules.meal.domain.model.Meal;
import io.github.jaovcarvalho.dietforge.modules.meal.domain.model.MealItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class DietQueryService {

    private final DietRepository dietRepository;

    public DietQueryService(DietRepository dietRepository) {
        this.dietRepository = dietRepository;
    }

    @Transactional(readOnly = true)
    public Diet byId (UUID dietId){
        return dietRepository.findWithMealsById(dietId)
                .orElseThrow(() -> new IllegalArgumentException("Diet not found: " + dietId));
    }

    @Transactional(readOnly = true)
    public Totals totalsOf (Diet diet){
        BigDecimal kcal = BigDecimal.ZERO;
        BigDecimal protein = BigDecimal.ZERO;
        BigDecimal carbs = BigDecimal.ZERO;
        BigDecimal fat = BigDecimal.ZERO;

        for (Meal meal : diet.getMeals()){
            for (MealItem mealItem : meal.getItems()){
                kcal = kcal.add(mealItem.getKcalResolved());
                protein = protein.add(mealItem.getProteinResolved());
                carbs =  carbs.add(mealItem.getCarbsResolved());
                fat =  fat.add(mealItem.getFatResolved());
            }
        }

        return new Totals(kcal, protein, carbs, fat);
    }
}
