import { http } from "@/services/http/client";
import type {
  UpdateMealRequest,
  AddMealItemRequest,
  PatchMealItemRequest
} from "@/types/diet.types";

const BASE_URL = "/api/v1/meals";

export async function updateMeal(mealId: string, payload: UpdateMealRequest) {
  return http(`${BASE_URL}/${mealId}`, {
    method: "PUT",
    body: JSON.stringify(payload),
  });
}

export async function addMealItem(mealId: string, payload: AddMealItemRequest) {
  return http(`${BASE_URL}/${mealId}/items`, {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export async function patchMealItem(mealId: string, mealItemId: string, payload: PatchMealItemRequest) {
  return http(`${BASE_URL}/${mealId}/items/${mealItemId}`, {
    method: "PATCH",
    body: JSON.stringify(payload),
  });
}

export async function deleteMealItem(mealId: string, mealItemId: string) {
  return http(`${BASE_URL}/${mealId}/items/${mealItemId}`, {
    method: "DELETE",
  });
}
