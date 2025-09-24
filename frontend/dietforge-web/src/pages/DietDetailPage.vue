<!-- src/pages/DietDetailPage.vue -->
<template>
  <div class="flex w-full flex-col gap-4 p-4" v-if="diet">
    <!-- Header -->
    <header class="flex items-center justify-between">
      <div>
        <h1 class="text-2xl font-semibold">{{ diet.name }}</h1>
        <p class="mt-1 text-sm text-gray-600">
          Total — Kcal: <b>{{ fmt(diet.totals?.kcal) }}</b>,
          Prot: <b>{{ fmt(diet.totals?.protein) }}</b>,
          Carb: <b>{{ fmt(diet.totals?.carbs) }}</b>,
          Gord: <b>{{ fmt(diet.totals?.fat) }}</b>
        </p>
      </div>
      <div class="flex items-center gap-2">
        <button class="rounded-md px-3 py-2 ring-1 ring-gray-300 hover:bg-gray-50" @click="openMealCreate">
          + Nova Refeição
        </button>
      </div>
    </header>

    <!-- Conteúdo -->
    <div v-if="loading" class="text-sm text-gray-500">Carregando...</div>
    <div v-else-if="error" class="text-sm text-red-600">{{ error }}</div>

    <section v-else class="flex flex-col gap-4">
      <article
        v-for="meal in diet.meals"
        :key="meal.id"
        class="rounded-lg border border-gray-200 p-4"
      >
        <header class="mb-3 flex items-center justify-between">
          <div>
            <h2 class="text-lg font-semibold">{{ meal.name }}</h2>
            <p class="mt-1 text-xs text-gray-600">
              Kcal: <b>{{ fmt(meal.totals?.kcal) }}</b>,
              Prot: <b>{{ fmt(meal.totals?.protein) }}</b>,
              Carb: <b>{{ fmt(meal.totals?.carbs) }}</b>,
              Gord: <b>{{ fmt(meal.totals?.fat) }}</b>
            </p>
          </div>
          <div class="flex items-center gap-2">
            <button class="rounded bg-blue-600 px-3 py-1 text-white hover:bg-blue-700" @click="openMealEdit(meal)">
              Editar
            </button>
            <button class="rounded bg-red-600 px-3 py-1 text-white hover:bg-red-700" @click="openMealDelete(meal)">
              Excluir
            </button>
          </div>
        </header>

        <!-- Tabela de itens -->
        <div class="overflow-x-auto rounded border border-gray-200">
          <table class="min-w-full text-left text-sm">
            <thead class="bg-gray-50 text-gray-600">
              <tr>
                <th class="px-4 py-3">Alimento</th>
                <th class="px-4 py-3">Qtd</th>
                <th class="px-4 py-3">Kcal</th>
                <th class="px-4 py-3">Prot</th>
                <th class="px-4 py-3">Carbo</th>
                <th class="px-4 py-3">Gord</th>
                <th class="px-4 py-3 text-right">Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="it in meal.items" :key="it.id" class="border-t hover:bg-gray-50">
                <td class="px-4 py-2">{{ it.foodName }}</td>
                <td class="px-4 py-2">
                  {{ it.quantityUsed }}
                  <span class="text-xs text-gray-500">{{ it.unitTypeUsed }}</span>
                </td>
                <td class="px-4 py-2">{{ fmt(it.totals?.kcal) }}</td>
                <td class="px-4 py-2">{{ fmt(it.totals?.protein) }}</td>
                <td class="px-4 py-2">{{ fmt(it.totals?.carbs) }}</td>
                <td class="px-4 py-2">{{ fmt(it.totals?.fat) }}</td>
                <td class="px-4 py-2">
                  <div class="flex items-center justify-end gap-2">
                    <button class="rounded bg-blue-600 px-3 py-1 text-white hover:bg-blue-700" @click="openItemEdit(meal, it)">
                      Editar
                    </button>
                    <button class="rounded bg-red-600 px-3 py-1 text-white hover:bg-red-700" @click="openItemDelete(meal, it)">
                      Excluir
                    </button>
                  </div>
                </td>
              </tr>
              <tr v-if="meal.items.length === 0">
                <td class="px-4 py-3 text-sm text-gray-500" colspan="7">Sem itens nesta refeição.</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Ações da refeição -->
        <div class="mt-3">
          <button class="rounded-md px-3 py-2 ring-1 ring-gray-300 hover:bg-gray-50" @click="openItemCreate(meal)">
            + Adicionar Item
          </button>
        </div>
      </article>
    </section>

    <!-- Modais -->
    <MealFormModal
      :open="showMealForm"
      :dietId="diet.id"
      :model="editingMeal"
      @close="showMealForm = false"
      @saved="reload()"
    />

    <MealItemFormModal
      :open="showItemForm"
      :mealId="selectedMealId || ''"
      :model="editingItem"
      @close="closeItemForm"
      @saved="reload()"
    />

    <ConfirmDeleteDialog
      :open="showDelete"
      @cancel="showDelete = false"
      @confirm="confirmDelete"
    />
  </div>

  <div v-else class="p-4">
    <p class="text-sm text-gray-500">Carregando...</p>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import { getDietById, deleteMealInDiet } from "@/services/api/diet.api";
import { deleteMealItem } from "@/services/api/meal.api";
import ConfirmDeleteDialog from "@/components/ConfirmDeleteDialog.vue";
import MealFormModal from "@/components/MealFormModal.vue";
import MealItemFormModal from "@/components/MealItemFormModal.vue";
import type { DietResponse, MealResponse, MealItemResponse } from "@/types/diet.types";

const route = useRoute();
const dietId = String(route.params.id);

const diet = ref<DietResponse | null>(null);
const loading = ref(false);
const error = ref<string | null>(null);

function fmt(v: unknown) {
  if (v == null) return "-";
  const n = Number(v);
  if (Number.isNaN(n)) return String(v);
  return Number.isInteger(n) ? String(n) : n.toFixed(1);
}

async function reload() {
  loading.value = true;
  error.value = null;
  try {
    diet.value = await getDietById(dietId);
  } catch (e: unknown) {
    const err = e as Error
    alert(err.message || "Erro ao carregar dieta")
  } finally {
    loading.value = false;
  }
}

/* ------- Meal: criar/editar/excluir ------- */
const showMealForm = ref(false);
const editingMeal = ref<{ id: string; name: string } | null>(null);

function openMealCreate() {
  editingMeal.value = null;
  showMealForm.value = true;
}
function openMealEdit(meal: MealResponse) {
  editingMeal.value = { id: meal.id, name: meal.name };
  showMealForm.value = true;
}

/* ------- Item: criar/editar/excluir ------- */
const showItemForm = ref(false);
const selectedMealId = ref<string | null>(null);
const editingItem = ref<MealItemResponse | null>(null);

function openItemCreate(meal: MealResponse) {
  selectedMealId.value = meal.id;
  editingItem.value = null;
  showItemForm.value = true;
}
function openItemEdit(meal: MealResponse, it: MealItemResponse) {
  selectedMealId.value = meal.id;
  editingItem.value = it;
  showItemForm.value = true;
}
function closeItemForm() {
  showItemForm.value = false;
  selectedMealId.value = null;
  editingItem.value = null;
}

/* ------- Confirm delete (meal or item) ------- */
const showDelete = ref(false);
let deleteCtx: { type: "meal" | "item"; mealId: string; itemId?: string } | null = null;

function openMealDelete(meal: MealResponse) {
  deleteCtx = { type: "meal", mealId: meal.id };
  showDelete.value = true;
}
function openItemDelete(meal: MealResponse, it: MealItemResponse) {
  deleteCtx = { type: "item", mealId: meal.id, itemId: it.id };
  showDelete.value = true;
}

async function confirmDelete() {
  if (!deleteCtx || !diet.value) return;
  try {
    if (deleteCtx.type === "meal") {
      await deleteMealInDiet(diet.value.id, deleteCtx.mealId);
    } else {
      await deleteMealItem(deleteCtx.mealId, deleteCtx.itemId!);
    }
    showDelete.value = false;
    deleteCtx = null;
    await reload();
  } catch (e) {
    console.error(e);
    showDelete.value = false;
    deleteCtx = null;
  }
}

onMounted(reload);
</script>
