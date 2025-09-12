<script setup lang="ts"></script>

<template>
  <div class="max-w-3xl mx-auto p-4">
    <h1 class="text-2xl font-semibold">DietForge</h1>
    <p class="text-gray-600">Bem-vindo! Escolha um módulo para começar.</p>

    <div class="mt-4">
      <router-link
        to="/calculos"
        class="inline-block px-4 py-2 rounded bg-blue-600 text-white hover:bg-blue-700"
      >
        Ir para Cálculos
      </router-link>
    </div>
  </div>
</template>
