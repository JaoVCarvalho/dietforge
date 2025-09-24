<!-- src/components/DietFormModal.vue -->
<template>
  <div v-if="open" class="fixed inset-0 z-50 flex items-center justify-center">
    <div class="absolute inset-0 bg-black/30" @click="onCancel"></div>

    <div class="relative z-10 w-full max-w-md rounded-lg bg-white p-5 shadow-lg" @click.stop>
      <header class="mb-3 flex items-center justify-between">
        <h2 class="text-lg font-semibold">
          {{ isEdit ? 'Editar Dieta' : 'Nova Dieta' }}
        </h2>
        <button class="rounded p-1 hover:bg-gray-100" @click="onCancel" aria-label="Fechar">
          ✕
        </button>
      </header>

      <form @submit.prevent="onSubmit" class="flex flex-col gap-3">
        <label class="text-sm">
          Nome
          <input
            v-model.trim="name"
            type="text"
            required
            class="mt-1 w-full rounded-md border border-gray-300 px-3 py-2"
            placeholder="Ex.: Cutting 2100 kcal"
          />
        </label>

        <p v-if="error" class="text-sm text-red-600">{{ error }}</p>

        <div class="mt-2 flex items-center justify-end gap-2">
          <button
            type="button"
            class="rounded-md px-3 py-2 ring-1 ring-gray-300"
            @click="onCancel"
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
import { createDiet, updateDiet } from '@/services/api/diet.api';

const props = defineProps<{
  open: boolean;
  // para edição: passe { id, name }
  model?: { id: string; name: string } | null;
}>();

const emit = defineEmits<{
  (e: 'close'): void;
  (e: 'saved'): void; // sinaliza sucesso para pai recarregar lista
}>();

const name = ref('');
const loading = ref(false);
const error = ref<string | null>(null);

const isEdit = computed(() => !!props.model?.id);

watch(
  () => props.open,
  (v) => {
    if (v) {
      // abrir: preenche com name atual (edição) ou vazio (criação)
      name.value = props.model?.name ?? '';
      error.value = null;
      loading.value = false;
    }
  },
);

function onCancel() {
  emit('close');
}

async function onSubmit() {
  if (!name.value) return;
  loading.value = true;
  error.value = null;
  try {
    if (isEdit.value && props.model?.id) {
      await updateDiet(props.model.id, { id: props.model.id, name: name.value });
    } else {
      await createDiet({ name: name.value });
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
