<!-- src/modules/foods/components/ConfirmDeleteDialog.vue -->
<template>
  <div v-if="open" class="fixed inset-0 z-50 flex items-center justify-center">
    <!-- Backdrop -->
    <div class="absolute inset-0 bg-black/40" @click="onCancel"></div>

    <!-- Dialog -->
    <div
      class="relative z-10 w-full max-w-md rounded-lg bg-white p-5 shadow-xl"
      role="dialog"
      aria-modal="true"
      @click.stop
    >
      <header class="mb-3 flex items-start justify-between">
        <h3 class="text-lg font-semibold">Confirmar exclusão</h3>
        <button class="rounded p-1 hover:bg-gray-100" @click="onCancel" aria-label="Fechar">✕</button>
      </header>

      <p class="text-sm text-gray-700">
        Tem certeza que deseja excluir
        <strong v-if="name">“{{ name }}”</strong>
        <span v-else>este item</span>?
        Essa ação não pode ser desfeita.
      </p>

      <footer class="mt-5 flex justify-end gap-2">
        <button
          class="rounded-md px-4 py-2 ring-1 ring-gray-300 hover:bg-gray-50"
          @click="onCancel"
        >
          {{ cancelText }}
        </button>
        <button
          class="rounded-md bg-red-600 px-4 py-2 font-medium text-white hover:bg-red-700"
          @click="onConfirm"
        >
          {{ confirmText }}
        </button>
      </footer>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount } from "vue";

const props = defineProps<{
  open: boolean;
  name?: string | null;
  confirmText?: string;
  cancelText?: string;
}>();

const emit = defineEmits<{
  cancel: [];
  confirm: [];
}>();

function onCancel() {
  emit("cancel");
}
function onConfirm() {
  emit("confirm");
}

// Fechar com ESC
function onKeydown(e: KeyboardEvent) {
  if (props.open && e.key === "Escape") onCancel();
}
onMounted(() => window.addEventListener("keydown", onKeydown));
onBeforeUnmount(() => window.removeEventListener("keydown", onKeydown));
</script>
