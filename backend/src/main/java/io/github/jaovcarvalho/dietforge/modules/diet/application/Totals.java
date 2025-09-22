package io.github.jaovcarvalho.dietforge.modules.diet.application;

import java.math.BigDecimal;

public record Totals(BigDecimal kcal,
                     BigDecimal protein,
                     BigDecimal carbs,
                     BigDecimal fat) {
}
