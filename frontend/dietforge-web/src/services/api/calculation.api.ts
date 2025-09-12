import { http } from "@/services/http/client";

import type {
    BmrRequest, BmrResponse,
    TdeeRequest, TdeeResponse,
    GoalRequest, GoalResponse
} from "@/types/calculation"

export function postBmr(payload: BmrRequest) {
    return http<BmrResponse>("/api/v1/calculations/bmr", {
        method: "POST",
        body: JSON.stringify(payload),
    })
}

export function postTdee(payload: TdeeRequest) {
    return http<TdeeResponse>("/api/v1/calculations/tdee", {
        method: "POST",
        body: JSON.stringify(payload),
    })
} 

export function postGoal(payload: GoalRequest) {
    return http<GoalResponse>("/api/v1/calculations/goal", {
        method: "POST",
        body: JSON.stringify(payload),
    })
}