<script setup lang="ts">
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import CalculationResult from '@/components/CalculationResult.vue';
import type { CalculationResultProps } from '@/types/types';

const router = useRouter();

const calculationResult = computed(() => {
  const state = router.options.history.state as { calculationResult?: CalculationResultProps };
  return state.calculationResult ?? null;
});
</script>

<template>
  <main>
    <CalculationResult
      v-if="calculationResult"
      :result="calculationResult"
      :show-save-button="true"
    />
    <p v-else class="empty-state">No calculation result found. Please calculate benefits first.</p>
  </main>
</template>

<style scoped>
main {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.empty-state {
  color: #52606d;
}
</style>
