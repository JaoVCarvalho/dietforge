package io.github.jaovcarvalho.dietforge.modules.diet.web;

import io.github.jaovcarvalho.dietforge.modules.diet.application.DietCommandService;
import io.github.jaovcarvalho.dietforge.modules.diet.application.DietQueryService;
import io.github.jaovcarvalho.dietforge.modules.diet.application.Totals;
import io.github.jaovcarvalho.dietforge.modules.diet.domain.model.Diet;
import io.github.jaovcarvalho.dietforge.modules.diet.web.dto.*;
import io.github.jaovcarvalho.dietforge.modules.food.web.dto.FoodResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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

    @PutMapping("/{dietId}")
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

    @GetMapping
    public Page<DietSummaryResponse> list(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        return queryService.search(q, page, size).map(this::toSummaryResponse);
    }


    private DietSummaryResponse toSummaryResponse(Diet diet){
        Totals dietTotals = queryService.totalsOf(diet);

        return new DietSummaryResponse(diet.getId(), diet.getName(), dietTotals);
    }

    private DietResponse toResponse(Diet diet) {

        Totals dietTotals = queryService.totalsOf(diet);

        List<DietMealSummary> meals = diet.getMeals().stream()
                .map(meal -> {
                    Totals mealTotals = queryService.totalsOf(meal);

                    List<DietMealItemSummary> items = meal.getItems().stream()
                            .map(it -> new DietMealItemSummary(
                                    it.getId(),
                                    it.getFood().getId(),
                                    it.getFood().getName(),
                                    it.getUnitTypeUsed(),
                                    it.getQuantityUsed(),
                                    new Totals(it.getKcalResolved(),
                                            it.getProteinResolved(),
                                            it.getCarbsResolved(),
                                            it.getFatResolved())
                            ))
                            .toList();

                    return new DietMealSummary(
                            meal.getId(),
                            meal.getName(),
                            items,
                            mealTotals
                    );
                })
                .toList();

        return new DietResponse(
                diet.getId(),
                diet.getName(),
                meals,
                dietTotals
        );
    }

}
