import type { ActivityLevel, GoalType } from "@/types/calculation";

export const ACTIVITY_FACTORS: Record<ActivityLevel, number> = {
    SEDENTARY: 1.2,
    LIGHT: 1.375,
    MODERATE: 1.55,
    VERY_ACTIVE: 1.725,
    ATHLETE: 1.9,
}

export const DEFAULT_GOAL_DELTAS: Record<Exclude<GoalType, "MAINTENANCE">, number> = {
    LIGHT_CUTTING: 500,
    MODERATE_CUTTING: 700,
    AGGRESSIVE_CUTTING: 1000,
    LIGHT_BULKING: 300,
    MODERATE_BULKING: 500,
    AGGRESSIVE_BULKING: 700,
}

export const MAINTENANCE_DELTA = 0;