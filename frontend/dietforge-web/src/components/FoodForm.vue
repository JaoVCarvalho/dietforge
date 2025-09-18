<!-- src/modules/foods/components/FoodForm.vue -->
<template>
  <form class="flex flex-col gap-3" @submit.prevent="onSubmit">
    <div class="grid grid-cols-1 gap-3 md:grid-cols-2">
      <div>
        <label class="block text-sm font-medium">Nome *</label>
        <input
          class="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 outline-none focus:ring-2 focus:ring-blue-500"
          v-model.trim="form.name"
          autocomplete="off"
        />
        <p v-if="errors.name" class="mt-1 text-sm text-red-600">{{ errors.name }}</p>
      </div>

      <div>
        <label class="block text-sm font-medium">Unidade *</label>
        <select
          class="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 outline-none focus:ring-2 focus:ring-blue-500"
          v-model="form.unitType"
        >
          <option value="" disabled>Selecione…</option>
          <option value="GRAM">GRAM</option>
          <option value="ML">ML</option>
          <option value="UNIT">UNIT</option>
        </select>
        <p v-if="errors.unitType" class="mt-1 text-sm text-red-600">{{ errors.unitType }}</p>
      </div>

      <div>
        <label class="block text-sm font-medium">Base Amount *</label>
        <input
          class="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 outline-none focus:ring-2 focus:ring-blue-500"
          v-model="form.baseAmountStr"
          inputmode="decimal"
          placeholder="Ex.: 100"
        />
        <p class="mt-1 text-xs text-gray-500">
          Valores nutricionais são por <strong>{{ form.baseAmountStr || '—' }}</strong> da unidade
          escolhida.
        </p>
        <p v-if="errors.baseAmount" class="mt-1 text-sm text-red-600">{{ errors.baseAmount }}</p>
      </div>

      <div>
        <label class="block text-sm font-medium">Kcal *</label>
        <input
          class="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 outline-none focus:ring-2 focus:ring-blue-500"
          v-model="form.kcalStr"
          inputmode="decimal"
          placeholder="Ex.: 130"
        />
        <p v-if="errors.kcal" class="mt-1 text-sm text-red-600">{{ errors.kcal }}</p>
      </div>

      <div>
        <label class="block text-sm font-medium">Proteína *</label>
        <input
          class="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 outline-none focus:ring-2 focus:ring-blue-500"
          v-model="form.proteinStr"
          inputmode="decimal"
          placeholder="Ex.: 2.5"
        />
        <p v-if="errors.protein" class="mt-1 text-sm text-red-600">{{ errors.protein }}</p>
      </div>

      <div>
        <label class="block text-sm font-medium">Carboidrato *</label>
        <input
          class="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 outline-none focus:ring-2 focus:ring-blue-500"
          v-model="form.carbsStr"
          inputmode="decimal"
          placeholder="Ex.: 28"
        />
        <p v-if="errors.carbs" class="mt-1 text-sm text-red-600">{{ errors.carbs }}</p>
      </div>

      <div>
        <label class="block text-sm font-medium">Gordura *</label>
        <input
          class="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 outline-none focus:ring-2 focus:ring-blue-500"
          v-model="form.fatStr"
          inputmode="decimal"
          placeholder="Ex.: 0.3"
        />
        <p v-if="errors.fat" class="mt-1 text-sm text-red-600">{{ errors.fat }}</p>
      </div>
    </div>

    <div class="mt-2 flex items-center justify-end gap-2">
      <button
        type="button"
        class="rounded-md px-4 py-2 ring-1 ring-gray-300 hover:bg-gray-50"
        @click="$emit('cancel')"
      >
        Cancelar
      </button>
      <button
        :disabled="submitting"
        class="rounded-md bg-blue-600 px-4 py-2 font-medium text-white hover:bg-blue-700 disabled:opacity-60"
      >
        {{ submitting ? 'Salvando…' : mode === 'edit' ? 'Salvar alterações' : 'Cadastrar' }}
      </button>
    </div>
  </form>
</template>

<script setup lang="ts">
import { reactive, watch } from 'vue';
import type {
  FoodResponse,
  CreateFoodRequest,
  UpdateFoodRequest,
  UnitType,
} from '@/types/food.types';

type Mode = 'create' | 'edit';

const props = defineProps<{
  mode: Mode;
  value?: FoodResponse | null; // quando editar
  submitting?: boolean;
}>();

const emit = defineEmits<{
  submit: [dto: CreateFoodRequest | UpdateFoodRequest];
  cancel: [];
}>();

// estado interno (strings para lidar com vírgula/ponto)
const form = reactive({
  id: '' as string,
  name: '',
  unitType: '' as '' | UnitType,
  baseAmountStr: '',
  kcalStr: '',
  proteinStr: '',
  carbsStr: '',
  fatStr: '',
});

const errors = reactive<Record<string, string | null>>({
  name: null,
  unitType: null,
  baseAmount: null,
  kcal: null,
  protein: null,
  carbs: null,
  fat: null,
});

watch(
  () => props.value,
  (v) => {
    if (!v) {
      // reset para criação
      form.id = '';
      form.name = '';
      form.unitType = '';
      form.baseAmountStr = '';
      form.kcalStr = '';
      form.proteinStr = '';
      form.carbsStr = '';
      form.fatStr = '';
      clearErrors();
      return;
    }
    // preenche para edição
    form.id = v.id;
    form.name = v.name;
    form.unitType = v.unitType;
    form.baseAmountStr = String(v.baseAmount);
    form.kcalStr = String(v.kcal);
    form.proteinStr = String(v.protein);
    form.carbsStr = String(v.carbs);
    form.fatStr = String(v.fat);
    clearErrors();
  },
  { immediate: true },
);

function clearErrors() {
  for (const k of Object.keys(errors)) errors[k] = null;
}

function parseNum(s: string): number | null {
  if (!s) return null;
  // aceita vírgula como decimal
  const n = Number(String(s).replace(',', '.'));
  return Number.isFinite(n) ? n : null;
}

function validate(): boolean {
  clearErrors();

  if (!form.name) errors.name = 'Informe o nome.';
  if (!form.unitType) errors.unitType = 'Selecione a unidade.';

  const baseAmount = parseNum(form.baseAmountStr);
  if (baseAmount === null || baseAmount <= 0) errors.baseAmount = 'Base deve ser maior que zero.';

  const kcal = parseNum(form.kcalStr);
  if (kcal === null || kcal < 0) errors.kcal = 'Kcal deve ser zero ou positivo.';

  const protein = parseNum(form.proteinStr);
  if (protein === null || protein < 0) errors.protein = 'Proteína deve ser zero ou positivo.';

  const carbs = parseNum(form.carbsStr);
  if (carbs === null || carbs < 0) errors.carbs = 'Carboidrato deve ser zero ou positivo.';

  const fat = parseNum(form.fatStr);
  if (fat === null || fat < 0) errors.fat = 'Gordura deve ser zero ou positivo.';

  return Object.values(errors).every((e) => !e);
}

function onSubmit() {
  if (!validate()) return;

  const dtoBase = {
    name: form.name.trim(),
    unitType: form.unitType as UnitType,
    baseAmount: parseNum(form.baseAmountStr)!,
    kcal: parseNum(form.kcalStr)!,
    protein: parseNum(form.proteinStr)!,
    carbs: parseNum(form.carbsStr)!,
    fat: parseNum(form.fatStr)!,
  };

  if (props.mode === 'edit') {
    const dto: UpdateFoodRequest = { id: form.id, ...dtoBase };
    emit('submit', dto);
  } else {
    const dto: CreateFoodRequest = { ...dtoBase };
    emit('submit', dto);
  }
}
</script>
