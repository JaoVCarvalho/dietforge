<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import type { Sex, ActivityLevel, GoalType } from '@/types/calculation';
import { ACTIVITY_FACTORS, DEFAULT_GOAL_DELTAS, MAINTENANCE_DELTA } from '@/constants/calculation';
import { postTdee, postGoal } from '@/services/api/calculation.api';

// ----- Estado do formulário (UI em PT-BR; código em inglês)
const sex = ref<Sex>('MALE');
const weightKg = ref<number | null>(null);
const heightCm = ref<number | null>(null);
const age = ref<number | null>(null);

const activityLevel = ref<ActivityLevel>('SEDENTARY');
const goalType = ref<GoalType>('MAINTENANCE');
const goalDelta = ref<number>(MAINTENANCE_DELTA); // editável quando não for manutenção

// fator fixo exibido ao escolher nível de atividade
const activityFactor = computed(() => ACTIVITY_FACTORS[activityLevel.value]);

// delta padrão exibido ao escolher goalType (permitir editar)
watch(goalType, (gt) => {
  if (gt === 'MAINTENANCE') {
    goalDelta.value = MAINTENANCE_DELTA;
  } else {
    goalDelta.value = DEFAULT_GOAL_DELTAS[gt];
  }
});

// ----- Estado de carregamento/erros/resultados
const loading = ref(false);
const errorMessage = ref<string | null>(null);

const bmr = ref<number | null>(null);
const bmrFormula = ref<string | null>(null);
const tdee = ref<number | null>(null);
const tdeeFormula = ref<string | null>(null);
const resultActivityFactor = ref<number | null>(null);

const targetCalories = ref<number | null>(null);

// validação simples (nível 1)
function validateForm(): string | null {
  if (!weightKg.value || weightKg.value <= 0) return 'Informe um peso válido (kg).';
  if (!heightCm.value || heightCm.value <= 0) return 'Informe uma altura válida (cm).';
  if (!age.value || age.value <= 0) return 'Informe uma idade válida.';
  return null;
}

async function handleCalculateAll() {
  errorMessage.value = null;
  loading.value = true;
  targetCalories.value = null;
  bmr.value = null;
  bmrFormula.value = null;
  tdee.value = null;
  tdeeFormula.value = null;
  resultActivityFactor.value = null;

  // 1) validação básica
  const err = validateForm();
  if (err) {
    loading.value = false;
    errorMessage.value = err;
    return;
  }

  try {
    // 2) TDEE (já vem com BMR)
    const tdeeRes = await postTdee({
      sex: sex.value,
      weightKg: weightKg.value!,
      heightCm: heightCm.value!,
      age: age.value!,
      activityLevel: activityLevel.value,
    });

    bmr.value = tdeeRes.bmr;
    bmrFormula.value = tdeeRes.formula;
    tdee.value = tdeeRes.tdee;
    tdeeFormula.value = tdeeRes.formula;
    resultActivityFactor.value = tdeeRes.activityFactor;

    // 3) GOAL (usa tdee calculado)
    const goalRes = await postGoal({
      tdee: tdee.value!,
      goalType: goalType.value,
      // se manutenção, não precisa mandar delta; se usuário alterou, enviamos
      delta: goalType.value === 'MAINTENANCE' ? null : goalDelta.value,
    });

    targetCalories.value = goalRes.targetCalories;
  } catch (e: unknown) {
    if (e instanceof Error) {
      errorMessage.value = e.message;
    } else {
      // casos não-Error (string, número, objeto de outra lib)
      errorMessage.value = String(e) || 'Erro ao calcular. Tente novamente.';
    }
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <div class="mx-auto max-w-4xl p-4 space-y-6">
    <h1 class="text-2xl font-semibold">Cálculos de Dieta</h1>
    <p class="text-sm text-gray-600">
      Preencha seus dados pessoais e clique em <strong>Calcular</strong> para obter BMR, TDEE e
      Calorias Alvo.
    </p>

    <!-- Formulário -->
    <form class="grid grid-cols-1 md:grid-cols-2 gap-4 bg-white p-4 rounded-2xl shadow">
      <!-- Sexo -->
      <div>
        <label for="sex" class="block text-sm font-medium mb-1">Sexo</label>
        <select id="sex" v-model="sex" class="w-full rounded border p-2">
          <option value="MALE">Masculino</option>
          <option value="FEMALE">Feminino</option>
        </select>
      </div>

      <!-- Idade -->
      <div>
        <label for="age" class="block text-sm font-medium mb-1">Idade</label>
        <input
          id="age"
          type="number"
          min="1"
          step="1"
          v-model.number="age"
          placeholder="ex.: 21"
          class="w-full rounded border p-2"
        />
      </div>

      <!-- Peso (kg) -->
      <div>
        <label for="weight" class="block text-sm font-medium mb-1">Peso (kg)</label>
        <input
          id="weight"
          type="number"
          min="1"
          step="0.1"
          v-model.number="weightKg"
          placeholder="ex.: 96.0"
          class="w-full rounded border p-2"
        />
      </div>

      <!-- Altura (cm) -->
      <div>
        <label for="height" class="block text-sm font-medium mb-1">Altura (cm)</label>
        <input
          id="height"
          type="number"
          min="1"
          step="0.1"
          v-model.number="heightCm"
          placeholder="ex.: 168.0"
          class="w-full rounded border p-2"
        />
      </div>

      <!-- Nível de atividade -->
      <div class="md:col-span-1">
        <label for="activityLevel" class="block text-sm font-medium mb-1">Nível de atividade</label>
        <select id="activityLevel" v-model="activityLevel" class="w-full rounded border p-2">
          <option value="SEDENTARY">Sedentário</option>
          <option value="LIGHT">Leve</option>
          <option value="MODERATE">Moderado</option>
          <option value="VERY_ACTIVE">Muito ativo</option>
          <option value="ATHLETE">Atleta</option>
        </select>
        <!-- fator fixo -->
        <p class="text-xs text-gray-600 mt-1">
          Fator: <strong>{{ activityFactor }}</strong>
        </p>
      </div>

      <!-- Objetivo -->
      <div class="md:col-span-1">
        <label for="goalType" class="block text-sm font-medium mb-1">Objetivo</label>
        <select id="goalType" v-model="goalType" class="w-full rounded border p-2">
          <option value="MAINTENANCE">Manutenção</option>
          <option value="LIGHT_CUTTING">Cutting Leve</option>
          <option value="MODERATE_CUTTING">Cutting Moderado</option>
          <option value="AGGRESSIVE_CUTTING">Cutting Agressivo</option>
          <option value="LIGHT_BULKING">Bulking Leve</option>
          <option value="MODERATE_BULKING">Bulking Moderado</option>
          <option value="AGGRESSIVE_BULKING">Bulking Agressivo</option>
        </select>

        <!-- delta padrão/editar -->
        <div class="mt-1">
          <label for="delta" class="block text-xs text-gray-600 mb-1">
            Variação (kcal) padrão do objetivo (alterável)
          </label>
          <input
            id="delta"
            type="number"
            step="1"
            :disabled="goalType === 'MAINTENANCE'"
            v-model.number="goalDelta"
            class="w-full rounded border p-2 disabled:bg-gray-100"
          />
          <p v-if="goalType === 'MAINTENANCE'" class="text-xs text-gray-500 mt-1">
            Em manutenção não há variação calórica.
          </p>
        </div>
      </div>

      <!-- Botão Calcular -->
      <div class="md:col-span-2 flex gap-3">
        <button
          type="button"
          @click="handleCalculateAll"
          :disabled="loading"
          class="px-4 py-2 rounded bg-blue-600 text-white hover:bg-blue-700 disabled:opacity-60"
        >
          {{ loading ? 'Calculando...' : 'Calcular' }}
        </button>
        <span v-if="errorMessage" class="text-sm text-red-600 self-center">{{ errorMessage }}</span>
      </div>
    </form>

    <!-- Resultados -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
      <section class="bg-white p-4 rounded-2xl shadow">
        <h2 class="font-semibold">BMR (Taxa Metabólica Basal)</h2>
        <p class="text-sm text-gray-600">Calorias mínimas para manter funções vitais em repouso.</p>
        <div class="mt-3 text-sm">
          <p><strong>Resultado:</strong> {{ bmr ?? '—' }}</p>
          <p><strong>Fórmula:</strong> {{ bmrFormula ?? '—' }}</p>
        </div>
      </section>

      <section class="bg-white p-4 rounded-2xl shadow">
        <h2 class="font-semibold">TDEE (Gasto Total Diário)</h2>
        <p class="text-sm text-gray-600">BMR ajustado pelo nível de atividade.</p>
        <div class="mt-3 text-sm">
          <p><strong>BMR:</strong> {{ bmr ?? '—' }}</p>
          <p><strong>Fator atividade (API):</strong> {{ resultActivityFactor ?? '—' }}</p>
          <p><strong>TDEE:</strong> {{ tdee ?? '—' }}</p>
          <p><strong>Fórmula:</strong> {{ tdeeFormula ?? '—' }}</p>
        </div>
      </section>

      <section class="bg-white p-4 rounded-2xl shadow">
        <h2 class="font-semibold">Meta (Calorias Alvo)</h2>
        <p class="text-sm text-gray-600">
          TDEE ajustado pelo objetivo (cutting/bulking/manutenção).
        </p>
        <div class="mt-3 text-sm">
          <p><strong>Alvo (kcal):</strong> {{ targetCalories ?? '—' }}</p>
        </div>
      </section>
    </div>
  </div>
</template>

<style scoped>
/* Mantemos o estilo no Tailwind; CSS aqui é opcional */
</style>
