package io.github.jaovcarvalho.dietforge.modules.calculation.domain.model;

public enum ActivityLevel {
    SEDENTARY(1.2),
    LIGHT(1.375),
    MODERATE(1.55),
    VERY_ACTIVE(1.725),
    ATHLETE(1.9);

    private final double factor;

    ActivityLevel(double factor) {
        this.factor = factor;
    }

    public double factor(){
        return factor;
    }
}
