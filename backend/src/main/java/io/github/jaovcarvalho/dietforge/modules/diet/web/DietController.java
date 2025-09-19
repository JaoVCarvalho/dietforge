package io.github.jaovcarvalho.dietforge.modules.diet.web;

import io.github.jaovcarvalho.dietforge.modules.diet.application.DietCommandService;
import io.github.jaovcarvalho.dietforge.modules.diet.application.DietQueryService;
import io.github.jaovcarvalho.dietforge.modules.diet.domain.model.Diet;
import io.github.jaovcarvalho.dietforge.modules.diet.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/diets")
public class DietController {

    private final DietCommandService commandService;
    private final DietQueryService queryService;

    public DietController(DietCommandService commandService, DietQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    public DietResponse create(@Valid @RequestBody CreateDietRequest request) {
        Diet diet = commandService.createDiet(request.getName());
        return toResponse(diet);
    }

    @PutMapping("/{id}")
    public DietResponse update(@PathVariable UUID dietId, @Valid @RequestBody UpdateDietRequest request) {
        // Reforço do Contrato Semântico do PUT
        if (!dietId.equals(request.id())) {
            throw new IllegalArgumentException("Path id and body id must match");
        }

        Diet updatedDiet = commandService.updateDiet(request);
        return toResponse(updatedDiet);
    }

    @DeleteMapping("/{dietId}")
    public void delete(@PathVariable UUID dietId) {
        commandService.delete(dietId);
    }

    @PostMapping("/{dietId}/meals")
    public DietResponse createMealInDiet(@PathVariable UUID dietId,
                                         @Valid @RequestBody CreateMealInDietRequest request) {
        commandService.createMealInDiet(dietId, request.getName());
        return toResponse(queryService.byId(dietId));
    }

    @DeleteMapping("/{dietId}/meals/{mealId}")
    public void removeMealInDiet(@PathVariable UUID dietId,
                                 @PathVariable UUID mealId) {
        commandService.removeMealInDiet(dietId, mealId);
    }

    @GetMapping("/{dietId}")
    public DietResponse get(@PathVariable UUID dietId) {
        return toResponse(queryService.byId(dietId));
    }



    private DietResponse toResponse(Diet diet) {
        var totals = queryService.totalsOf(diet);

        List<DietMealSummary> meals = diet.getMeals().stream().map(meal -> {
            List<DietMealItemSummary> items = meal.getItems().stream().map(it ->
                    new DietMealItemSummary(
                            it.getId(),
                            it.getFood().getId(),
                            it.getFood().getName(),
                            it.getUnitTypeUsed(),
                            it.getQuantityUsed(),
                            it.getKcalResolved(),
                            it.getProteinResolved(),
                            it.getCarbsResolved(),
                            it.getFatResolved()
                    )
            ).toList();

            return new DietMealSummary(meal.getId(), meal.getName(), items);
        }).toList();

        return new DietResponse(
                diet.getId(),
                diet.getName(),
                meals,
                totals.kcal(),
                totals.protein(),
                totals.carbs(),
                totals.fat()
        );
    }
}
