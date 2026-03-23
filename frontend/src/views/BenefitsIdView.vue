<script setup lang="ts">
import { watch } from 'vue';
import {useRoute, useRouter} from 'vue-router';
import { useBenefitById } from '@/composables/useBenefitsApi';
import CalculationResult from '@/components/CalculationResult.vue';

const router = useRouter();
const route = useRoute();
const { isLoading, errorMessage, benefitResponse, fetchBenefitById } = useBenefitById();

watch(
  () => route.params.id,
  (idParam) => {
    void fetchBenefitById(String(idParam ?? ''));
  },
  { immediate: true },
);
</script>

<template>
  <main>
    <p v-if="isLoading" class="status">Loading benefit details...</p>
    <div v-else-if="errorMessage" class="error-container">
      <p class="error">{{ errorMessage }}</p>
      <button class="back-btn" @click="router.push('/')">← Back</button>
    </div>
    <CalculationResult
      v-else-if="benefitResponse"
      :result="{
        message: benefitResponse.message,
        data: benefitResponse.data.calculationResult,
        grossSalary: benefitResponse.data.grossSalary,
        babyBirthDate: benefitResponse.data.babyBirthDate,
        errors: benefitResponse.errors,
      }"
    />
  </main>
</template>

<style scoped>
main {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.status {
  font-size: 0.9rem;
  color: var(--c-text-muted);
}

.error-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 1.5rem;
}

.error {
  font-size: 0.9rem;
  color: var(--c-error);
  font-weight: 500;
}

.back-btn {
  padding: 0.4rem 0.9rem;
  background: transparent;
  color: var(--c-text-muted);
  border: 1px solid var(--c-border);
  border-radius: var(--radius-sm);
  font-family: var(--font-family);
  font-size: 0.83rem;
  cursor: pointer;
  transition: background 0.2s, color 0.2s;
}

.back-btn:hover {
  background: var(--c-warm-gray);
  color: var(--c-text);
}
</style>
