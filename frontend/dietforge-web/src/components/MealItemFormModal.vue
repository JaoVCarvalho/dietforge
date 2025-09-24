<!-- src/components/MealItemFormModal.vue -->
<template>
  <div v-if="open" class="fixed inset-0 z-50 flex items-center justify-center">
    <div class="absolute inset-0 bg-black/30" @click="$emit('close')"></div>

    <div class="relative z-10 w-full max-w-lg rounded-lg bg-white p-5 shadow-lg" @click.stop>
      <header class="mb-3 flex items-center justify-between">
        <h2 class="text-lg font-semibold">
          {{ isEdit ? 'Editar Item' : 'Novo Item' }}
        </h2>
        <button class="rounded p-1 hover:bg-gray-100" @click="$emit('close')" aria-label="Fechar">
          ✕
        </button>
      </header>

      <form @submit.prevent="onSubmit" class="flex flex-col gap-3">
        <!-- Autocomplete de Food -->
        <div>
          <label class="text-sm">Alimento</label>
          <input
            v-model="foodQuery"
            type="text"
            class="mt-1 w-full rounded-md border border-gray-300 px-3 py-2"
            placeholder="Buscar alimento..."
            @input="onFoodInput"
            :disabled="loading || (isEdit && lockFood)"
          />
          <div
            v-if="showFoods"
            class="mt-1 max-h-52 overflow-auto rounded border border-gray-200 bg-white"
          >
            <button
              v-for="f in foods"
              :key="f.id"
              type="button"
              class="flex w-full items-center justify-between px-3 py-2 text-left hover:bg-gray-50"
              @click="selectFood(f.id, f.name)"
            >
              <span>{{ f.name }}</span>
              <span class="text-xs text-gray-500">({{ f.unitType }}/{{ f.baseAmount }})</span>
            </button>
            <div v-if="foods.length === 0" class="px-3 py-2 text-sm text-gray-500">
              Nenhum alimento encontrado.
            </div>
          </div>
          <p v-if="selectedFoodName" class="mt-1 text-xs text-gray-600">
            Selecionado: <b>{{ selectedFoodName }}</b>
          </p>
        </div>

        <!-- Quantidade -->
        <label class="text-sm">
          Quantidade usada
          <input
            v-model.number="quantity"
            type="number"
            min="0"
            step="0.01"
            required
            class="mt-1 w-full rounded-md border border-gray-300 px-3 py-2"
            placeholder="Ex.: 100"
          />
        </label>

        <p v-if="error" class="text-sm text-red-600">{{ error }}</p>

        <div class="mt-2 flex items-center justify-end gap-2">
          <button
            type="button"
            class="rounded-md px-3 py-2 ring-1 ring-gray-300"
            @click="$emit('close')"
            :disabled="loading"
          >
            Cancelar
          </button>
          <button
            type="submit"
            class="rounded-md px-3 py-2 font-medium ring-1 ring-gray-300 hover:bg-gray-50"
            :disabled="loading"
          >
            {{ loading ? 'Salvando...' : 'Salvar' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue';
import { searchFoods } from '@/services/api/food.api';
import { addMealItem, patchMealItem } from '@/services/api/meal.api';
import type { MealItemResponse } from '@/types/diet.types';

const props = defineProps<{
  open: boolean;
  mealId: string;
  // se editar, passamos o item atual
  model?: MealItemResponse | null;
}>();
const emit = defineEmits<{ (e: 'close'): void; (e: 'saved'): void }>();

const loading = ref(false);
const error = ref<string | null>(null);

const isEdit = computed(() => !!props.model?.id);
// em edição, muitas vezes não trocamos o alimento (só quantidade). Se quiser permitir troca, defina lockFood=false
const lockFood = true;

const foodQuery = ref('');
const foods = ref<Array<{ id: string; name: string; unitType: string; baseAmount: number }>>([]);
const showFoods = ref(false);
const selectedFoodId = ref<string | null>(null);
const selectedFoodName = ref<string | null>(null);

const quantity = ref<number>(0);

watch(
  () => props.open,
  async (v) => {
    if (v) {
      error.value = null;
      loading.value = false;

      if (isEdit.value && props.model) {
        selectedFoodId.value = props.model.foodId;
        selectedFoodName.value = props.model.foodName;
        foodQuery.value = props.model.foodName;
        quantity.value = Number(props.model.quantityUsed) || 0;
        showFoods.value = false;
      } else {
        selectedFoodId.value = null;
        selectedFoodName.value = null;
        foodQuery.value = '';
        quantity.value = 0;
        foods.value = [];
        showFoods.value = false;
      }
    }
  },
);

let debounceTimer: ReturnType<typeof setTimeout> | null = null;

function onFoodInput() {
  if (isEdit.value && lockFood) return;
  showFoods.value = true;
  if (debounceTimer) clearTimeout(debounceTimer);
  debounceTimer = setTimeout(fetchFoods, 250);
}

async function fetchFoods() {
  try {
    const res = await searchFoods(foodQuery.value, 0, 10);
    foods.value = res.content;
  } catch {
    foods.value = [];
  }
}
function selectFood(id: string, name: string) {
  selectedFoodId.value = id;
  selectedFoodName.value = name;
  foodQuery.value = name;
  showFoods.value = false;
}

async function onSubmit() {
  loading.value = true;
  error.value = null;
  try {
    if (isEdit.value && props.model) {
      await patchMealItem(props.mealId, props.model.id, {
        // se não quiser permitir troca de food, envie somente quantidade
        // foodId: selectedFoodId.value ?? undefined,
        quantityUpdated: quantity.value,
      });
    } else {
      if (!selectedFoodId.value) {
        throw new Error('Selecione um alimento');
      }
      await addMealItem(props.mealId, {
        foodId: selectedFoodId.value,
        quantityUsed: quantity.value,
      });
    }
    emit('saved');
    emit('close');
  } catch (e: unknown) {
    const err = e as Error;
    alert(err.message || 'Erro ao salvar');
  } finally {
    loading.value = false;
  }
}
</script>
