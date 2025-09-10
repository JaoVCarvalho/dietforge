package io.github.jaovcarvalho.dietforge.modules.food.application;

import io.github.jaovcarvalho.dietforge.modules.food.domain.model.Food;
import io.github.jaovcarvalho.dietforge.modules.food.infra.FoodRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FoodQueryService {

    public final FoodRepository foodRepository;

    public FoodQueryService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Transactional(readOnly = true)
    public Food byId(UUID id){
        return foodRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("food not found: " + id));
    }

    // page → define o índice da página
    // size → quantidade de registros por página
    @Transactional(readOnly = true)
    public Page<Food> search(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        if (q == null || q.isBlank()) return foodRepository.findAll(pageable);
        return foodRepository.findByNameContainingIgnoreCase(q.trim(), pageable);
    }
}
