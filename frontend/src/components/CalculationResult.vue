<script setup lang="ts">
import { computed, ref } from 'vue';
import Card from '@/components/Card.vue';
import { useRouter } from 'vue-router';
import { useCreateBenefit } from '@/composables/useBenefitsApi';
import type { CalculationResultProps } from '@/types/types';

const router = useRouter();
const { createBenefit, isLoading, errorMessage } = useCreateBenefit();
const responseId = ref<string | null>(null);

const props = withDefaults(
  defineProps<{
    result: CalculationResultProps;
    showSaveButton?: boolean;
  }>(),
  {
    showSaveButton: false,
  },
);

const monthlyEligibleSalary = computed(() => props.result.data.monthlyEligibleSalary.toFixed(2));
const dailyRate = computed(() => props.result.data.dailyRate.toFixed(2));
const totalPayment = computed(() => props.result.data.totalPayment.toFixed(2));

function formatMonthYear(year: number, month: number): string {
  return new Date(year, month - 1).toLocaleDateString('en-GB', {
    year: 'numeric',
    month: 'long',
  });
}

async function saveCalculation() {
  const data = await createBenefit({
    grossSalary: props.result.grossSalary,
    babyBirthDate: props.result.babyBirthDate,
  });

  if (data) {
    responseId.value = data.data.id;
  }
}
</script>

<template>
  <Card>
    <div class="header">
      <h2>Calculation result</h2>
      <button class="back-btn" @click="router.push('/')">Back</button>
    </div>

    <p class="message">{{ result.message }}</p>

    <div class="summary-grid">
      <div class="tile">
        <span class="tile-label">Monthly eligible salary</span>
        <span class="tile-value">{{ monthlyEligibleSalary }} <span class="tile-unit">EUR</span></span>
      </div>
      <div class="tile">
        <span class="tile-label">Daily rate</span>
        <span class="tile-value">{{ dailyRate }} <span class="tile-unit">EUR</span></span>
      </div>
      <div class="tile tile--total">
        <span class="tile-label">Total payment</span>
        <span class="tile-value">{{ totalPayment }} <span class="tile-unit">EUR</span></span>
      </div>
    </div>

    <h3 class="breakdown-title">Monthly breakdown</h3>

    <div class="table-wrapper">
      <table>
        <thead>
          <tr>
            <th>Month</th>
            <th>Payable days</th>
            <th>Payment amount (EUR)</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in result.data.breakdown" :key="`${item.year}-${item.month}`">
            <td>{{ formatMonthYear(item.year, item.month) }}</td>
            <td>{{ item.payableDays }}</td>
            <td>{{ item.paymentAmount.toFixed(2) }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    <div v-if="responseId" class="save-success">
      <p class="save-success-title">Saved successfully</p>
      <p class="save-success-id">Your ID: <strong>{{ responseId }}</strong></p>
    </div>

    <div v-if="props.showSaveButton && !responseId" class="actions">
      <button class="save-btn" @click="saveCalculation" :disabled="isLoading">
        {{ isLoading ? 'Saving…' : 'Save this calculation' }}
      </button>
    </div>
  </Card>
</template>

<style scoped>
h2 {
  font-size: 1.05rem;
  font-weight: 600;
  color: var(--c-text);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.25rem;
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

.message {
  font-size: 0.85rem;
  color: var(--c-text-muted);
  font-weight: 300;
  margin-bottom: 1.25rem;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(190px, 1fr));
  gap: 0.75rem;
  margin: 1.25rem 0 1.75rem;
}

.tile {
  background: var(--c-sage-light);
  border-radius: var(--radius-md);
  padding: 1rem 1.1rem;
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
}

.tile--total {
  background: var(--c-warm-gray);
}

.tile-label {
  font-size: 0.75rem;
  font-weight: 500;
  color: var(--c-text-muted);
}

.tile-value {
  font-size: 1.35rem;
  font-weight: 600;
  color: var(--c-text);
  line-height: 1.2;
}

.tile-unit {
  font-size: 0.8rem;
  font-weight: 400;
  color: var(--c-text-muted);
}

.breakdown-title {
  font-size: 0.9rem;
  font-weight: 600;
  color: var(--c-text);
  margin-bottom: 0.75rem;
}

.table-wrapper {
  overflow-x: auto;
  border-radius: var(--radius-md);
  border: 1px solid var(--c-border);
}

table {
  width: 100%;
  border-collapse: collapse;
}

thead tr {
  background: var(--c-warm-gray);
}

th {
  padding: 0.6rem 1rem;
  font-size: 0.75rem;
  font-weight: 600;
  color: var(--c-text-muted);
  text-align: left;
}

td {
  padding: 0.6rem 1rem;
  font-size: 0.875rem;
  color: var(--c-text);
  border-top: 1px solid var(--c-border);
}

tbody tr:hover td {
  background: var(--c-sage-light);
}

.right {
  text-align: right;
}

.amount {
  font-weight: 600;
  color: var(--c-sage-dark);
}

/* Actions */
.actions {
  margin-top: 1.5rem;
}

.save-btn {
  padding: 0.6rem 1.4rem;
  background: var(--c-sage);
  color: var(--c-white);
  border: none;
  border-radius: var(--radius-sm);
  font-family: var(--font-family);
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  box-shadow: var(--shadow-btn);
  transition: background 0.2s;
}

.save-btn:hover:not(:disabled) {
  background: var(--c-sage-dark);
}

.save-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  box-shadow: none;
}

/* Feedback */
.error {
  margin-top: 1rem;
  background: var(--c-error-light);
  border: 1px solid #ECC8C8;
  color: var(--c-error);
  font-size: 0.83rem;
  padding: 0.5rem 0.85rem;
  border-radius: var(--radius-sm);
}

.save-success {
  margin-top: 1.25rem;
  background: var(--c-sage-light);
  border: 1px solid #C0D9CA;
  border-radius: var(--radius-md);
  padding: 0.9rem 1.1rem;
}

.save-success-title {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--c-sage-dark);
}

.save-success-id {
  font-size: 0.83rem;
  color: var(--c-text-muted);
  margin-top: 0.15rem;
}

.save-success-id strong {
  font-family: monospace;
  color: var(--c-text);
}
</style>
