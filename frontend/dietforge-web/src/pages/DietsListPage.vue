<!-- src/pages/DietsListPage.vue -->
<template>
  <div class="flex w-full flex-col gap-4 p-4">
    <!-- Cabeçalho -->
    <header class="flex items-center justify-between">
      <h1 class="text-2xl font-semibold">Diets</h1>
      <button
        class="rounded-md px-4 py-2 font-medium shadow-sm ring-1 ring-gray-300 hover:bg-gray-50"
        @click="openCreate()"
      >
        + Nova Dieta
      </button>
    </header>

    <!-- Filtros -->
    <div class="flex items-center gap-2">
      <input
        v-model="q"
        type="text"
        placeholder="Buscar por nome..."
        class="w-full rounded-md border border-gray-300 px-3 py-2"
        @keyup.enter="fetchDiets(0)"
      />
      <button class="rounded-md px-3 py-2 ring-1 ring-gray-300 hover:bg-gray-50" @click="fetchDiets(0)">
        Buscar
      </button>
      <button v-if="q" class="rounded-md px-3 py-2 ring-1 ring-gray-300 hover:bg-gray-50" @click="clearSearch">
        Limpar
      </button>
    </div>

    <!-- Estados -->
    <div v-if="loading" class="text-sm text-gray-500">Carregando...</div>
    <div v-else-if="error" class="text-sm text-red-600">{{ error }}</div>

    <!-- Tabela -->
    <div v-else>
      <div v-if="page.content.length === 0" class="text-sm text-gray-500">Nenhuma dieta encontrada.</div>

      <div v-else class="overflow-x-auto rounded-lg border border-gray-200">
        <table class="min-w-full text-left text-sm">
          <thead class="bg-gray-50 text-gray-600">
            <tr>
              <th class="px-4 py-3">Nome</th>
              <th class="px-4 py-3">Kcal</th>
              <th class="px-4 py-3">Proteína</th>
              <th class="px-4 py-3">Carbo</th>
              <th class="px-4 py-3">Gordura</th>
              <th class="px-4 py-3 text-right">Ações</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="diet in page.content" :key="diet.id" class="border-t hover:bg-gray-50">
              <td class="px-4 py-2 font-medium">
                <router-link
                  class="underline decoration-dotted"
                  :to="{ name: 'diet-detail', params: { id: diet.id } }"
                >
                  {{ diet.name }}
                </router-link>
              </td>
              <td class="px-4 py-2">{{ fmt(diet.totals?.kcal) }}</td>
              <td class="px-4 py-2">{{ fmt(diet.totals?.protein) }}</td>
              <td class="px-4 py-2">{{ fmt(diet.totals?.carbs) }}</td>
              <td class="px-4 py-2">{{ fmt(diet.totals?.fat) }}</td>
              <td class="px-4 py-2">
                <div class="flex items-center justify-end gap-2">
                  <button class="rounded bg-blue-600 px-3 py-1 text-white hover:bg-blue-700" @click="openEdit(diet)">
                    Editar
                  </button>
                  <button class="rounded bg-red-600 px-3 py-1 text-white hover:bg-red-700" @click="openDelete(diet)">
                    Excluir
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Paginação -->
      <div class="mt-3 flex items-center justify-between text-sm">
        <span>Página {{ page.number + 1 }} de {{ page.totalPages || 1 }}</span>
        <div class="flex items-center gap-2">
          <button
            class="rounded-md px-3 py-1 ring-1 ring-gray-300 disabled:opacity-50"
            :disabled="page.number === 0"
            @click="fetchDiets(page.number - 1)"
          >
            ← Anterior
          </button>
          <button
            class="rounded-md px-3 py-1 ring-1 ring-gray-300 disabled:opacity-50"
            :disabled="page.number + 1 >= page.totalPages"
            @click="fetchDiets(page.number + 1)"
          >
            Próxima →
          </button>
        </div>
      </div>
    </div>

    <!-- Modais -->
    <DietFormModal
      :open="showForm"
      :model="editing"
      @close="closeForm"
      @saved="fetchDiets(0)"
    />

    <ConfirmDeleteDialog
      :open="showDelete"
      @cancel="showDelete = false"
      @confirm="confirmDelete"
    >
      <!-- Caso seu ConfirmDeleteDialog suporte slots, o texto abaixo aparece;
           caso não suporte, ele só usa o padrão do componente. -->
      <template #default>
        Tem certeza que deseja excluir a dieta <b>{{ editing?.name }}</b>?
      </template>
    </ConfirmDeleteDialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { searchDiets, deleteDiet } from "@/services/api/diet.api";
import DietFormModal from "@/components/DietFormModal.vue";
import ConfirmDeleteDialog from "@/components/ConfirmDeleteDialog.vue";
import type { DietSummaryResponse } from "@/types/diet.types";
import type { Page } from "@/types/food.types";

const loading = ref(false);
const error = ref<string | null>(null);
const q = ref("");

const page = reactive<Page<DietSummaryResponse>>({
  content: [],
  number: 0,
  size: 10,
  totalElements: 0,
  totalPages: 0,
});

const showForm = ref(false);
const showDelete = ref(false);
const editing = ref<{ id: string; name: string } | null>(null);

function fmt(v: unknown) {
  if (v == null) return "-";
  const n = Number(v);
  if (Number.isNaN(n)) return String(v);
  return Number.isInteger(n) ? String(n) : n.toFixed(1);
}

async function fetchDiets(p = 0) {
  loading.value = true;
  error.value = null;
  try {
    const res = await searchDiets(q.value, p, page.size);
    Object.assign(page, res);
  } catch (e: unknown) {
    const err = e as Error
    alert(err.message || "Erro ao buscar dietas")
  } finally {
    loading.value = false;
  }
}

function clearSearch() {
  q.value = "";
  fetchDiets(0);
}

function openCreate() {
  editing.value = null;
  showForm.value = true;
}

function openEdit(d: DietSummaryResponse) {
  editing.value = { id: d.id, name: d.name };
  showForm.value = true;
}

function closeForm() {
  showForm.value = false;
}

function openDelete(d: DietSummaryResponse) {
  editing.value = { id: d.id, name: d.name };
  showDelete.value = true;
}

async function confirmDelete() {
  if (!editing.value?.id) return;

  // 1) otimista: já remove da UI
  const idToRemove = editing.value.id;
  const wasLastOnPage = page.content.length === 1 && page.number > 0;

  page.content = page.content.filter(d => d.id !== idToRemove);
  page.totalElements = Math.max(0, page.totalElements - 1);

  try {
    await deleteDiet(idToRemove);

    showDelete.value = false;
    editing.value = null;

    // 2) refetch para sincronizar números (totalPages, etc.)
    const nextPage = wasLastOnPage ? page.number - 1 : page.number;
    await fetchDiets(nextPage);
  } catch (e) {
    // se der erro, faz um refetch para restaurar estado real
    console.error(e);
    showDelete.value = false;
    await fetchDiets(page.number);
  }
}


onMounted(() => fetchDiets(0));
</script>
