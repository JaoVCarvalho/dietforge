<!-- src/modules/foods/pages/FoodsListPage.vue -->
<template>
  <div class="flex w-full flex-col gap-4 p-4">
    <!-- Cabeçalho -->
    <header class="flex items-center justify-between">
      <h1 class="text-2xl font-semibold">Foods</h1>
      <button
        class="rounded-md px-4 py-2 font-medium shadow-sm ring-1 ring-gray-300 hover:bg-gray-50"
        @click="openCreate"
      >
        + Novo Food
      </button>
    </header>

    <!-- Filtros -->
    <section>
      <FoodFilters :query="query" @update:query="handleSearch" />
    </section>

    <!-- Tabela -->
    <section>
      <FoodTable
        :rows="rows"
        :loading="loading"
        :page="page"
        :size="size"
        :total="total"
        @edit="openEdit"
        @delete="confirmDelete"
        @change:page="setPage"
        @change:size="setSize"
      />
    </section>

    <!-- Modal de formulário -->
    <FoodFormModal
      :open="isModalOpen"
      :foodId="selectedId"
      @close="closeModal"
      @saved="refreshAfterSave"
    />

    <!-- Diálogo de exclusão -->
    <ConfirmDeleteDialog
      :open="isDeleteOpen"
      :name="selectedName"
      @cancel="isDeleteOpen = false"
      @confirm="removeSelected"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from "vue";
import FoodFilters from "../components/FoodFilters.vue";
import FoodTable from "../components/FoodTable.vue";
import FoodFormModal from "../components/FoodFormModal.vue";
import ConfirmDeleteDialog from "../components/ConfirmDeleteDialog.vue";
import { searchFoods, deleteFood } from "@/services/api/food.api";
import type { FoodResponse, Page } from "@/types/food.types";

// Estado da página
const query = ref("");
const loading = ref(false);

const page = ref(0);
const size = ref(10);
const total = ref(0);
const rows = ref<FoodResponse[]>([]);

// Controle do modal
const isModalOpen = ref(false);
const selectedId = ref<string | null>(null);

// Controle da exclusão
const isDeleteOpen = ref(false);
const selectedName = ref<string | null>(null);
const selectedDeleteId = ref<string | null>(null);

// Debounce simples
let debounceHandle: number | undefined;

async function fetchList() {
  loading.value = true;
  try {
    const res: Page<FoodResponse> = await searchFoods(query.value, page.value, size.value);
    rows.value = res.content;
    total.value = res.totalElements;
    // garante que page não passe do total
    const maxPage = Math.max(0, Math.ceil(res.totalElements / res.size) - 1);
    if (page.value > maxPage) page.value = maxPage;
  } catch (e: unknown) {
    const err = e as Error;
    alert(err.message || "Falha ao carregar lista.");
  } finally {
    loading.value = false;
  }
}

function scheduleFetch() {
  // debounce 400ms
  if (debounceHandle) window.clearTimeout(debounceHandle);
  debounceHandle = window.setTimeout(() => fetchList(), 400);
}

// Ações do modal
function openCreate() {
  selectedId.value = null;
  isModalOpen.value = true;
}
function openEdit(id: string) {
  selectedId.value = id;
  isModalOpen.value = true;
}
function closeModal() {
  isModalOpen.value = false;
  selectedId.value = null;
}
async function refreshAfterSave() {
  closeModal();
  await fetchList();
  alert("Food salvo com sucesso.");
}

// Ações de exclusão
function confirmDelete(payload: { id: string; name: string }) {
  selectedDeleteId.value = payload.id;
  selectedName.value = payload.name;
  isDeleteOpen.value = true;
}
async function removeSelected() {
  if (!selectedDeleteId.value) return;
  try {
    loading.value = true;
    await deleteFood(selectedDeleteId.value);
    // se a última linha da página foi removida, ajustar página para não ficar vazia
    if (rows.value.length === 1 && page.value > 0) {
      page.value = page.value - 1;
    }
    await fetchList();
  } catch (e: unknown) {
    const err = e as Error;
    alert(err.message || "Falha ao excluir.");
  } finally {
    isDeleteOpen.value = false;
    selectedDeleteId.value = null;
    selectedName.value = null;
    loading.value = false;
  }
}

// Busca e paginação
function handleSearch(newQuery: string) {
  query.value = newQuery;
  page.value = 0;
  scheduleFetch();
}
function setPage(newPage: number) {
  page.value = Math.max(0, newPage);
  scheduleFetch();
}
function setSize(newSize: number) {
  size.value = newSize;
  page.value = 0;
  scheduleFetch();
}

// Carregar na montagem
onMounted(fetchList);

// Reagir a mudanças (caso altere manualmente os refs)
watch([query, page, size], scheduleFetch);
</script>
