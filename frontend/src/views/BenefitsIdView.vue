<script setup lang="ts">
import { watch } from 'vue';
import { useRoute } from 'vue-router';
import { useBenefitById } from '@/composables/useBenefitsApi';
import CalculationResult from '@/components/CalculationResult.vue';

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
    <p v-else-if="errorMessage" class="error">{{ errorMessage }}</p>
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

.error {
  font-size: 0.9rem;
  color: var(--c-error);
  font-weight: 500;
}
</style>
