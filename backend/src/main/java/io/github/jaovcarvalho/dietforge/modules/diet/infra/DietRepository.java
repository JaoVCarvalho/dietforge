package io.github.jaovcarvalho.dietforge.modules.diet.infra;

import io.github.jaovcarvalho.dietforge.modules.diet.domain.model.Diet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DietRepository extends JpaRepository<Diet, UUID> {

    boolean existsByNameIgnoreCase(String name);

    // útil para carregar dieta já com meals (evita N+1 em algumas consultas)
    @EntityGraph(attributePaths = {"meals"})
    Optional<Diet> findWithMealsById(UUID id);

    Page<Diet> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @EntityGraph(attributePaths = {"meals"})
    Page<Diet> findAll(Pageable pageable);
}
