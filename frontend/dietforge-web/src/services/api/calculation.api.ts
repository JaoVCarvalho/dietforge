import { http } from "@/services/http/client";

import type {
    BmrRequest, BmrResponse,
    TdeeRequest, TdeeResponse,
    GoalRequest, GoalResponse
} from "@/types/calculation"

const BASE_URL = "/api/v1/calculations"

export function postBmr(payload: BmrRequest) {
    return http<BmrResponse>(`${BASE_URL}/bmr`, {
        method: "POST",
        body: JSON.stringify(payload),
    })
}

export function postTdee(payload: TdeeRequest) {
    return http<TdeeResponse>(`${BASE_URL}/tdee`, {
        method: "POST",
        body: JSON.stringify(payload),
    })
} 

export function postGoal(payload: GoalRequest) {
    return http<GoalResponse>(`${BASE_URL}/goal`, {
        method: "POST",
        body: JSON.stringify(payload),
    })
}