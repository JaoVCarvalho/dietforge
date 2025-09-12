<!-- src/App.vue -->
<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="min-h-screen bg-gray-50 text-gray-900">
    <!-- Cabeçalho -->
    <header class="bg-white border-b">
      <div class="mx-auto max-w-6xl px-4 py-3 flex items-center justify-between">
        <RouterLink to="/" class="text-lg font-semibold">
          DietForge
        </RouterLink>

        <nav class="flex items-center gap-4 text-sm">
          <RouterLink to="/" class="hover:underline">
            Início
          </RouterLink>
          <RouterLink to="/calculos" class="hover:underline">
            Cálculos
          </RouterLink>
        </nav>
      </div>
    </header>

    <!-- Conteúdo -->
    <main class="mx-auto max-w-6xl px-4 py-6">
      <RouterView />
    </main>
  </div>
</template>
