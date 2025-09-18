<template>
  <div class="flex items-center gap-2">
    <input
      type="text"
      class="w-full max-w-md rounded-md border border-gray-300 px-3 py-2 outline-none focus:ring-2 focus:ring-blue-500"
      :value="query"
      placeholder="Buscar por nome..."
      @input="onInput"
    />
    <button
      class="rounded-md px-3 py-2 ring-1 ring-gray-300 hover:bg-gray-50"
      @click="clear"
      :disabled="!query"
    >
      Limpar
    </button>
  </div>
</template>

<script setup lang="ts">
defineProps<{ query: string }>();
const emit = defineEmits<{ 'update:query': [value: string] }>();

function onInput(e: Event) {
  emit('update:query', (e.target as HTMLInputElement).value);
}

function clear() {
  emit('update:query', '');
}
</script>
