<!-- src/App.vue -->
<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="min-h-screen bg-gray-50 text-gray-900 flex flex-col">
    <!-- Cabeçalho -->
    <header class="bg-white border-b">
      <div class="px-6 py-3 flex items-center justify-between">
        <RouterLink to="/" class="text-lg font-semibold">
          DietForge
        </RouterLink>

        <nav class="flex items-center gap-6 text-sm">
          <RouterLink to="/" class="hover:underline">
            Início
          </RouterLink>
          <RouterLink to="/calculations" class="hover:underline">
            Cálculos
          </RouterLink>
          <RouterLink to="/foods" class="hover:underline">
            Alimentos
          </RouterLink>
          <RouterLink to="/diets" class="hover:underline">
            Dietas
          </RouterLink>
        </nav>
      </div>
    </header>

    <main class="flex-1 flex items-center justify-center px-4 py-6">
      <RouterView />
    </main>
  </div>
</template>
