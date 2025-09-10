package io.github.jaovcarvalho.dietforge.modules.calculation.domain.formula;

import io.github.jaovcarvalho.dietforge.modules.calculation.domain.model.Sex;

// Strategy Pattern
public interface BmrFormula {

    double calculate(Sex sex, double weightKg, double heightCm, Integer age);
    String name();
}
