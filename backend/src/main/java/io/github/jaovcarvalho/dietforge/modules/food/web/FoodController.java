package io.github.jaovcarvalho.dietforge.modules.food.web;


import io.github.jaovcarvalho.dietforge.modules.food.application.FoodCommandService;
import io.github.jaovcarvalho.dietforge.modules.food.application.FoodQueryService;
import io.github.jaovcarvalho.dietforge.modules.food.domain.model.Food;
import io.github.jaovcarvalho.dietforge.modules.food.web.dto.CreateFoodRequest;
import io.github.jaovcarvalho.dietforge.modules.food.web.dto.FoodResponse;
import io.github.jaovcarvalho.dietforge.modules.food.web.dto.PatchFoodRequest;
import io.github.jaovcarvalho.dietforge.modules.food.web.dto.UpdateFoodRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

    // Boa prática → Reforço do Contrato Semântico do PUT
    @PutMapping("/{id}")
    public FoodResponse update(@PathVariable UUID id, @RequestBody @Valid UpdateFoodRequest request){
        if (!id.equals(request.id())){
            throw new IllegalArgumentException("Path id and body id must match");
        }
        Food foodUpdated = commandService.update(request);
        return toResponse(foodUpdated);
    }

    @PatchMapping("/{id}")
    public FoodResponse patch(@PathVariable UUID id, @RequestBody @Valid PatchFoodRequest request){
        Food foodUpdated = commandService.patch(id, request);
        return toResponse(foodUpdated);
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id){
        commandService.delete(id);
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
