<!-- src/modules/foods/components/FoodTable.vue -->
<template>
  <div class="w-full">
    <table class="w-full border-collapse border border-gray-300">
      <thead class="bg-gray-100">
        <tr>
          <th class="border border-gray-300 px-2 py-1 text-left">Nome</th>
          <th class="border border-gray-300 px-2 py-1">Unidade</th>
          <th class="border border-gray-300 px-2 py-1">Base</th>
          <th class="border border-gray-300 px-2 py-1">Kcal</th>
          <th class="border border-gray-300 px-2 py-1">Proteína</th>
          <th class="border border-gray-300 px-2 py-1">Carbo</th>
          <th class="border border-gray-300 px-2 py-1">Gordura</th>
          <th class="border border-gray-300 px-2 py-1">Ações</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="!loading && rows.length === 0">
          <td colspan="8" class="border px-2 py-6 text-center text-gray-500">
            Nenhum resultado.
          </td>
        </tr>
        <tr v-for="food in rows" :key="food.id" class="hover:bg-gray-50">
          <td class="border px-2 py-1">{{ food.name }}</td>
          <td class="border px-2 py-1 text-center">{{ food.unitType }}</td>
          <td class="border px-2 py-1 text-right">{{ food.baseAmount }}</td>
          <td class="border px-2 py-1 text-right">{{ food.kcal }}</td>
          <td class="border px-2 py-1 text-right">{{ food.protein }}</td>
          <td class="border px-2 py-1 text-right">{{ food.carbs }}</td>
          <td class="border px-2 py-1 text-right">{{ food.fat }}</td>
          <td class="border px-2 py-1 text-center">
            <button
              class="mr-2 rounded bg-blue-500 px-2 py-1 text-white hover:bg-blue-600"
              @click="$emit('edit', food.id)"
            >
              Editar
            </button>
            <button
              class="rounded bg-red-500 px-2 py-1 text-white hover:bg-red-600"
              @click="$emit('delete', { id: food.id, name: food.name })"
            >
              Excluir
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Paginação -->
    <div class="mt-4 flex items-center justify-between">
      <button
        class="rounded bg-gray-200 px-3 py-1"
        @click="$emit('change:page', page - 1)"
        :disabled="page <= 0 || loading"
      >
        Anterior
      </button>
      <span>Página {{ page + 1 }} de {{ totalPages }}</span>
      <button
        class="rounded bg-gray-200 px-3 py-1"
        @click="$emit('change:page', page + 1)"
        :disabled="page >= totalPages - 1 || loading"
      >
        Próxima
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import type { FoodResponse } from "@/types/food.types";

const props = defineProps<{
  rows: FoodResponse[];
  page: number;
  size: number;
  total: number;
  loading: boolean;
}>();

const totalPages = computed(() =>
  props.total > 0 ? Math.ceil(props.total / props.size) : 1,
);
</script>
