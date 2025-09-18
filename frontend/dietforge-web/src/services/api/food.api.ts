import { http } from "@/services/http/client";
import type { FoodResponse, CreateFoodRequest, UpdateFoodRequest, Page, PatchFoodRequest } from "@/types/food.types"

const BASE_URL = "/api/v1/foods";

export async function searchFoods(
  q: string = "",
  page: number = 0,
  size: number = 10,
): Promise<Page<FoodResponse>> {
  const params = new URLSearchParams();
  if (q) params.append("q", q);
  params.append("page", page.toString());
  params.append("size", size.toString());

  return http<Page<FoodResponse>>(`${BASE_URL}?${params.toString()}`);
}

export async function getFoodById(id: string): Promise<FoodResponse> {
  return http<FoodResponse>(`${BASE_URL}/${id}`);
}

export async function createFood(payload: CreateFoodRequest): Promise<FoodResponse> {
  return http<FoodResponse>(BASE_URL, {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export async function updateFood(id: string, payload: UpdateFoodRequest): Promise<FoodResponse> {
  return http<FoodResponse>(`${BASE_URL}/${id}`, {
    method: "PUT",
    body: JSON.stringify(payload),
  });
}

export async function patchFood(id: string, payload: PatchFoodRequest) {
  return http<FoodResponse>(`${BASE_URL}/${id}`, {
    method: "PATCH",
    body: JSON.stringify(payload),
  });
}


export async function deleteFood(id: string): Promise<void> {
  return http<void>(`${BASE_URL}/${id}`, {
    method: "DELETE",
  });
}
