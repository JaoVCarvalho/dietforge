export type UnitType = "GRAM" | "ML" | "UNIT";

export interface Food {
    id: string;
    name: string;
    unitType: UnitType;
    baseAmount: number;
    kcal: number;
    protein: number;
    carbs: number;
    fat: number;
}

export interface CreateFoodDTO {
    name: string;
    unitType: UnitType;
    baseAmount: number;
    kcal: number;
    protein: number;
    carbs: number;
    fat: number;
}

export interface UpdateFoodDTO {
    id: string;
    name: string;
    unitType: UnitType;
    baseAmount: number;
    kcal: number;
    protein: number;
    carbs: number;
    fat: number;
}

export interface PatchFoodDTO {
    name: string;
    unitType: UnitType;
    baseAmount: number;
    kcal: number;
    protein: number;
    carbs: number;
    fat: number;
}

export interface Page<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
}