package io.github.jaovcarvalho.dietforge.modules.calculation.application;

import io.github.jaovcarvalho.dietforge.modules.calculation.domain.formula.MifflinStJeorFormula;
import io.github.jaovcarvalho.dietforge.modules.calculation.domain.model.ActivityLevel;
import io.github.jaovcarvalho.dietforge.modules.calculation.domain.model.GoalType;
import io.github.jaovcarvalho.dietforge.modules.calculation.domain.model.Sex;
import io.github.jaovcarvalho.dietforge.modules.calculation.domain.service.BmrCalculator;
import io.github.jaovcarvalho.dietforge.modules.calculation.domain.service.TdeeCalculator;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    private final BmrCalculator bmrCalculator = new BmrCalculator(new MifflinStJeorFormula());
    private final TdeeCalculator tdeeCalculator = new TdeeCalculator();

    public double calculateBmr(Sex sex, double kg, double cm, int age){
        return bmrCalculator.bmr(sex, kg, cm, age);
    }

    public double calculateTdee(Sex sex, double kg, double cm, int age, ActivityLevel activityLevel){
        double bmr = calculateBmr(sex, kg, cm, age);
        return tdeeCalculator.tdee(bmr, activityLevel);
    }

    public double targetCalories(double tdee, GoalType goalType, Double delta){
        return switch (goalType){
            case LIGHT_CUTTING -> tdee - (delta != null ? delta : 500);
            case MODERATE_CUTTING -> tdee - (delta != null ? delta : 700);
            case AGGRESSIVE_CUTTING -> tdee - (delta != null ? delta : 1000);
            case LIGHT_BULKING -> tdee + (delta != null ? delta : 300);
            case MODERATE_BULKING -> tdee + (delta != null ? delta : 500);
            case AGGRESSIVE_BULKING -> tdee + (delta != null ? delta : 700);
            case MAINTENANCE -> tdee;
        };
    }

    public String formulaName(){
        return bmrCalculator.formulaName();
    }
}
