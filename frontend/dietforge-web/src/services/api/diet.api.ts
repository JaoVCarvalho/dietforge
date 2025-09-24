// src/services/api/diet.api.ts
import { http } from "@/services/http/client";
import type {
  DietResponse,
  DietSummaryResponse,
  CreateDietRequest,
  UpdateDietRequest,
  CreateMealInDietRequest
} from "@/types/diet.types";
import type { Page } from "@/types/food.types";

const BASE_URL = "/api/v1/diets";

export async function searchDiets(
  q = "",
  page = 0,
  size = 10
): Promise<Page<DietSummaryResponse>> {
  const params = new URLSearchParams();
  if (q) params.append("q", q);
  params.append("page", String(page));
  params.append("size", String(size));
  return http<Page<DietSummaryResponse>>(`${BASE_URL}?${params.toString()}`);
}

export async function getDietById(id: string): Promise<DietResponse> {
  return http<DietResponse>(`${BASE_URL}/${id}`);
}

export async function createDiet(payload: CreateDietRequest) {
  return http(`${BASE_URL}`, {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export async function updateDiet(id: string, payload: UpdateDietRequest) {
  return http(`${BASE_URL}/${id}`, {
    method: "PUT",
    body: JSON.stringify(payload),
  });
}

export async function deleteDiet(id: string): Promise<void> {
  return http<void>(`${BASE_URL}/${id}`, { method: "DELETE" });
}

// Meals dentro da Diet (DietController)
export async function createMealInDiet(dietId: string, payload: CreateMealInDietRequest) {
  return http(`${BASE_URL}/${dietId}/meals`, {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export async function deleteMealInDiet(dietId: string, mealId: string): Promise<void> {
  return http<void>(`${BASE_URL}/${dietId}/meals/${mealId}`, {
    method: "DELETE",
  });
}
