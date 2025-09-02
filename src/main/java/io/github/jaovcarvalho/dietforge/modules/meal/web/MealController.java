package io.github.jaovcarvalho.dietforge.modules.meal.web;

import io.github.jaovcarvalho.dietforge.modules.meal.application.MealCommandService;
import io.github.jaovcarvalho.dietforge.modules.meal.application.MealQueryService;
import io.github.jaovcarvalho.dietforge.modules.meal.domain.Meal;
import io.github.jaovcarvalho.dietforge.modules.meal.web.dto.AddMealItemRequest;
import io.github.jaovcarvalho.dietforge.modules.meal.web.dto.CreateMealRequest;
import io.github.jaovcarvalho.dietforge.modules.meal.web.dto.MealItemResponse;
import io.github.jaovcarvalho.dietforge.modules.meal.web.dto.MealResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/meals")
public class MealController {

    private final MealCommandService commandService;
    private final MealQueryService queryService;

    public MealController(MealCommandService commandService, MealQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    public MealResponse create(@Valid @RequestBody CreateMealRequest request){
        Meal meal = commandService.createMeal(request.getName());
        return toResponse(meal);
    }

    @GetMapping("/{mealId}")
    public MealResponse get(@PathVariable UUID mealId){
        Meal meal = queryService.byId(mealId);
        return toResponse(meal);
    }

    @PostMapping("/{mealId}/items")
    public MealResponse addItem(@PathVariable UUID mealId, @RequestBody  AddMealItemRequest request){
        commandService.addItem(mealId,
                request.getFoodId(),
                request.getUnitTypeUsed(),
                request.getQuantityUsed());

        Meal meal = queryService.byId(mealId);
        return toResponse(meal);
    }

    @DeleteMapping("/{mealId}/items/{mealItemId}")
    public MealResponse removeItem(@PathVariable UUID mealId, @PathVariable UUID mealItemId){
        commandService.removeItem(mealId, mealItemId);
        Meal meal = queryService.byId(mealId);
        return toResponse(meal);
    }



    private MealResponse toResponse(Meal meal){
        var totals = queryService.totalsOf(meal);
        List<MealItemResponse> items = meal.getItems().stream().map(it ->
                new MealItemResponse(
                 it.getId(),
                 it.getFood().getId(),
                 it.getFood().getName(),
                 it.getUnitType(),
                 it.getQuantityUsed(),
                 it.getKcalResolved(),
                 it.getProteinResolved(),
                 it.getCarbsResolved(),
                 it.getFatResolved()
                )
        ).toList();

        return new MealResponse(
                meal.getId(),
                meal.getName(),
                items,
                totals.kcal(),
                totals.protein(),
                totals.carbs(),
                totals.fat()
        );
    }
}
