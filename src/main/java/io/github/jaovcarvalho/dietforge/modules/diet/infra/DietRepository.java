package io.github.jaovcarvalho.dietforge.modules.diet.infra;

import io.github.jaovcarvalho.dietforge.modules.diet.domain.model.Diet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DietRepository extends JpaRepository<Diet, UUID> {

    boolean existsByNameIgnoreCase(String name);

    // útil para carregar dieta já com meals (evita N+1 em algumas consultas)
//    @EntityGraph(attributePaths = {"meals", "meals.items", "meals.items.food"})
    @EntityGraph(attributePaths = {"meals"})
    Optional<Diet> findWithMealsById(UUID id);
}
