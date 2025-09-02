package io.github.jaovcarvalho.dietforge.modules.meal.application;

import io.github.jaovcarvalho.dietforge.modules.meal.domain.Meal;
import io.github.jaovcarvalho.dietforge.modules.meal.domain.MealItem;
import io.github.jaovcarvalho.dietforge.modules.meal.infra.MealRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class MealQueryService {

    private final MealRepository mealRepository;

    public MealQueryService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Transactional(readOnly = true)
    public Meal byId(UUID mealId){
        return mealRepository.findById(mealId)
                .orElseThrow(() -> new IllegalArgumentException("Meal not found: " + mealId));
    }

    @Transactional(readOnly = true)
    public Totals totalsOf(Meal meal){
        BigDecimal kcal = BigDecimal.ZERO;
        BigDecimal protein = BigDecimal.ZERO;
        BigDecimal carbs = BigDecimal.ZERO;
        BigDecimal fat = BigDecimal.ZERO;

        for(MealItem it : meal.getItems()){
            kcal = kcal.add(it.getKcalResolved());
            protein = protein.add(it.getProteinResolved());
            carbs = carbs.add(it.getCarbsResolved());
            fat = fat.add(it.getFatResolved());
        }

        return new Totals(kcal, protein, carbs, fat);
    }
}
