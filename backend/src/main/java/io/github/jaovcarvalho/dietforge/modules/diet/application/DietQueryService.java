package io.github.jaovcarvalho.dietforge.modules.diet.application;

import io.github.jaovcarvalho.dietforge.modules.diet.domain.model.Diet;
import io.github.jaovcarvalho.dietforge.modules.diet.infra.DietRepository;
import io.github.jaovcarvalho.dietforge.modules.meal.domain.model.Meal;
import io.github.jaovcarvalho.dietforge.modules.meal.domain.model.MealItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Diet byId(UUID dietId) {
        return dietRepository.findWithMealsById(dietId)
                .orElseThrow(() -> new IllegalArgumentException("Diet not found: " + dietId));
    }

    @Transactional(readOnly = true)
    public Page<Diet> search(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        if (q == null || q.isBlank()) return dietRepository.findAll(pageable);
        return dietRepository.findByNameContainingIgnoreCase(q.trim(), pageable);
    }

    @Transactional(readOnly = true)
    public Totals totalsOf(Meal meal) {
        BigDecimal kcal = BigDecimal.ZERO;
        BigDecimal protein = BigDecimal.ZERO;
        BigDecimal carbs = BigDecimal.ZERO;
        BigDecimal fat = BigDecimal.ZERO;

        for (MealItem mealItem : meal.getItems()) {
            kcal = kcal.add(mealItem.getKcalResolved());
            protein = protein.add(mealItem.getProteinResolved());
            carbs = carbs.add(mealItem.getCarbsResolved());
            fat = fat.add(mealItem.getFatResolved());
        }

        return new Totals(kcal, protein, carbs, fat);
    }

    @Transactional(readOnly = true)
    public Totals totalsOf(Diet diet) {
        Totals dietTotals = new Totals(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

        for (Meal meal : diet.getMeals()) {
            Totals mealTotals = totalsOf(meal);
            dietTotals = plus(dietTotals, mealTotals);
        }

        return dietTotals;
    }


    private Totals plus(Totals a, Totals b) {
        return new Totals(
                a.kcal().add(b.kcal()),
                a.protein().add(b.protein()),
                a.carbs().add(b.carbs()),
                a.fat().add(b.fat())
        );
    }
}
