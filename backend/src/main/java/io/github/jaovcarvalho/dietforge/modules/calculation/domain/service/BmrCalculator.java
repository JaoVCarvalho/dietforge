package io.github.jaovcarvalho.dietforge.modules.calculation.domain.service;

import io.github.jaovcarvalho.dietforge.modules.calculation.domain.formula.BmrFormula;
import io.github.jaovcarvalho.dietforge.modules.calculation.domain.model.Sex;

public class BmrCalculator {

    private final BmrFormula formula;

    // Strategy Pattern
    public BmrCalculator(BmrFormula formula) {
        this.formula = formula;
    }

    public double bmr(Sex sex, double kg, double cm, int age){
        return formula.calculate(sex, kg, cm, age);
    }

    public String formulaName(){
        return formula.name();
    }
}
