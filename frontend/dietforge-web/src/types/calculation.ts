export type Sex = "MALE" | "FEMALE";

export type ActivityLevel =
  | "SEDENTARY"
  | "LIGHT"
  | "MODERATE"
  | "VERY_ACTIVE"
  | "ATHLETE";

export interface BmrRequest {
    sex: Sex;
    weightKg: number;
    heightCm: number;
    age: number;
}

export interface BmrResponse {
    bmr: number;
    formula: string;
}

export interface TdeeRequest extends BmrRequest {
    activityLevel: ActivityLevel;
}

export interface TdeeResponse {
    bmr: number;
    activityFactor: number;
    tdee: number;
    formula: string;
}

export type GoalType = 
    | "LIGHT_CUTTING"
    | "MODERATE_CUTTING"
    | "AGGRESSIVE_CUTTING"
    | "LIGHT_BULKING"
    | "MODERATE_BULKING"
    | "AGGRESSIVE_BULKING"
    | "MAINTENANCE";

export interface GoalRequest {
    tdee: number;
    goalType: GoalType;
    delta?: number | null;
}

export interface GoalResponse {
    targetCalories: number;
}