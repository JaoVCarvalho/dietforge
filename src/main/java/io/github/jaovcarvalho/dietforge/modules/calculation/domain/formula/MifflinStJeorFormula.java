package io.github.jaovcarvalho.dietforge.modules.calculation.domain.formula;

import io.github.jaovcarvalho.dietforge.modules.calculation.domain.model.Sex;

public class MifflinStJeorFormula implements BmrFormula{

    @Override
    public double calculate(Sex sex, double weightKg, double heightCm, Integer age) {
        double base = (10 * weightKg) + (6.25 * heightCm) - (5 * age);
        return (sex == Sex.MALE) ? base + 5 : base - 161;
    }

    @Override
    public String name() {
        return "Mifflin-St Jeor";
    }
}
