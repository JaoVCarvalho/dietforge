<!-- src/modules/foods/components/FoodFormModal.vue -->
<template>
  <div v-if="open" class="fixed inset-0 z-50 flex items-center justify-center">
    <!-- Backdrop -->
    <div class="absolute inset-0 bg-black/40" @click="handleClose"></div>

    <!-- Conteúdo -->
    <div class="relative z-10 w-full max-w-2xl rounded-lg bg-white p-5 shadow-xl">
      <header class="mb-4 flex items-center justify-between">
        <h2 class="text-xl font-semibold">
          {{ isEdit ? 'Editar Food' : 'Novo Food' }}
        </h2>
        <button class="rounded p-1 hover:bg-gray-100" @click="handleClose">✕</button>
      </header>

      <FoodForm
        :mode="isEdit ? 'edit' : 'create'"
        :value="food"
        :submitting="submitting"
        @submit="handleSubmit"
        @cancel="handleClose"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, watch, ref } from 'vue';
import FoodForm from './FoodForm.vue';
import type { FoodResponse, CreateFoodRequest, UpdateFoodRequest } from '@/types/food.types';
import { getFoodById, createFood, updateFood } from '@/services/api/food.api';

const props = defineProps<{
  open: boolean;
  foodId?: string | null; // se vier, é edição
}>();

const emit = defineEmits<{
  close: [];
  saved: [food: FoodResponse];
}>();

const isEdit = computed(() => !!props.foodId);
const food = ref<FoodResponse | null>(null);
const submitting = ref(false);

async function loadIfEdit() {
  food.value = null;
  if (props.open && props.foodId) {
    try {
      submitting.value = true;
      food.value = await getFoodById(props.foodId);
    } catch {
      alert('Não foi possível carregar o Food.');
      handleClose();
    } finally {
      submitting.value = false;
    }
  }
}

watch(() => props.open, () => loadIfEdit());
watch(() => props.foodId, () => loadIfEdit());

function handleClose() {
  emit('close');
}

async function handleSubmit(dto: CreateFoodRequest | UpdateFoodRequest) {
  try {
    submitting.value = true;
    const saved = isEdit.value
      ? await updateFood((dto as UpdateFoodRequest).id, dto as UpdateFoodRequest)
      : await createFood(dto as CreateFoodRequest);

    emit('saved', saved);
  } catch (e: unknown) {
    const err = e as Error;
    alert(err?.message || 'Falha ao salvar.');
  } finally {
    submitting.value = false;
  }
}
</script>
