package io.github.jaovcarvalho.dietforge.modules.food.web;


import io.github.jaovcarvalho.dietforge.modules.food.application.FoodCommandService;
import io.github.jaovcarvalho.dietforge.modules.food.application.FoodQueryService;
import io.github.jaovcarvalho.dietforge.modules.food.domain.model.Food;
import io.github.jaovcarvalho.dietforge.modules.food.web.dto.CreateFoodRequest;
import io.github.jaovcarvalho.dietforge.modules.food.web.dto.FoodResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/foods")
public class FoodController {

    private final FoodCommandService commandService;
    private final FoodQueryService queryService;

    public FoodController(FoodCommandService commandService, FoodQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    public FoodResponse create(@Valid @RequestBody CreateFoodRequest req){
        Food food = commandService.create(
                req.getName(),
                req.getUnitType(),
                req.getBaseAmount(),
                req.getKcal(),
                req.getProtein(),
                req.getCarbs(),
                req.getFat());

        return toResponse(food);
    }

    @GetMapping("/{id}")
    public FoodResponse get(@PathVariable UUID id){
        return toResponse(queryService.byId(id));
    }

    @GetMapping
    public Page<FoodResponse> list(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        return queryService.search(q, page, size).map(this::toResponse);
    }

    private FoodResponse toResponse(Food food){
        return new FoodResponse(
                food.getId(),
                food.getName(),
                food.getUnitType(),
                food.getBaseAmount(),
                food.getKcal(),
                food.getProtein(),
                food.getCarbs(),
                food.getFat());
    }
}
