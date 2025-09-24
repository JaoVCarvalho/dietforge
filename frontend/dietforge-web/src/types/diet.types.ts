// src/types/diet.types.ts
export interface Totals {
  kcal: number;
  protein: number;
  carbs: number;
  fat: number;
}

export type UnitType = "GRAM" | "ML" | "UNIT";

export interface MealItemResponse {
  id: string;
  foodId: string;
  foodName: string;
  unitTypeUsed: UnitType;
  quantityUsed: number;
  totals: Totals;
}

export interface MealResponse {
  id: string;
  name: string;
  items: MealItemResponse[];
  totals: Totals;
}

export interface DietResponse {
  id: string;
  name: string;
  meals: MealResponse[];
  totals: Totals;
}

export interface DietSummaryResponse {
  id: string;
  name: string;
  totals: Totals;
}

export interface CreateDietRequest {
  name: string;
}
export interface UpdateDietRequest {
  id: string;
  name: string;
}

export interface CreateMealInDietRequest {
  name: string;
}

export interface UpdateMealRequest {
  id: string;
  name: string;
}

export interface AddMealItemRequest {
  foodId: string;
  quantityUsed: number;
}

export interface PatchMealItemRequest {
  foodId?: string | null;
  quantityUpdated?: number | null;
}





