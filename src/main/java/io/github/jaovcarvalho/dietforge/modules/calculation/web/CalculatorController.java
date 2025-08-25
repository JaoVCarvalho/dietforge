package io.github.jaovcarvalho.dietforge.modules.calculation.web;

import io.github.jaovcarvalho.dietforge.modules.calculation.application.CalculatorService;
import io.github.jaovcarvalho.dietforge.modules.calculation.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/calculations")
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @PostMapping("/bmr")
    public BmrResponse bmr(@Valid @RequestBody BmrRequest req){
        double bmr =  calculatorService.calculateBmr(req.sex(), req.weightKg(), req.heightCm(), req.age());
        return new BmrResponse(bmr, calculatorService.formulaName());
    }

    @PostMapping("/tdee")
    public TdeeResponse tdee(@Valid @RequestBody TdeeRequest req){
        double bmr = calculatorService.calculateBmr(req.sex(), req.weightKg(), req.heightCm(), req.age());
        double tdee = calculatorService.calculateTdee(req.sex(), req.weightKg(), req.heightCm(), req.age(), req.activityLevel());
        return new TdeeResponse(bmr, req.activityLevel().factor(), tdee, calculatorService.formulaName());
    }

    @PostMapping("/goal")
    public GoalResponse goal(@Valid @RequestBody GoalRequest req){
        return new GoalResponse(calculatorService.targetCalories(req.tdee(), req.goalType(), req.delta()));
    }
}
