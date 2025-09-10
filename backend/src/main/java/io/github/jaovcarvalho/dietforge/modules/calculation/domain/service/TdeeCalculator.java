package io.github.jaovcarvalho.dietforge.modules.calculation.domain.service;

import io.github.jaovcarvalho.dietforge.modules.calculation.domain.model.ActivityLevel;

public class TdeeCalculator {

    public double tdee(double bmr, ActivityLevel level){
        return (bmr * level.factor());
    }
}
