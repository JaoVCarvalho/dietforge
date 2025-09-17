export type UnitType = "GRAM" | "ML" | "UNIT";

export interface FoodResponse {
    id: string;
    name: string;
    unitType: UnitType;
    baseAmount: number;
    kcal: number;
    protein: number;
    carbs: number;
    fat: number;
}

export interface CreateFoodRequest {
    name: string;
    unitType: UnitType;
    baseAmount: number;
    kcal: number;
    protein: number;
    carbs: number;
    fat: number;
}

export interface UpdateFoodRequest {
    id: string;
    name: string;
    unitType: UnitType;
    baseAmount: number;
    kcal: number;
    protein: number;
    carbs: number;
    fat: number;
}

// Partial -> Transforma todos os campos em opcionais (ex.: name?: string;)
export type PatchFoodRequest = Partial<Omit<UpdateFoodRequest, "id">>;

export interface Page<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
}