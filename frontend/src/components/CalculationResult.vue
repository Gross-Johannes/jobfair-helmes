<script setup lang="ts">
import { computed, ref } from 'vue';
import Card from '@/components/Card.vue';
import { useRouter } from 'vue-router';
import type { CalculationResultProps } from '@/types/types';

const router = useRouter();

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
</script>

<template>
  <Card>
    <div class="header">
      <h2>Calculation result</h2>
      <button @click="router.push('/')">Back</button>
    </div>

    <p class="message">{{ result.message }}</p>

    <div class="summary-grid">
      <article>
        <h3>Monthly eligible salary</h3>
        <p>{{ monthlyEligibleSalary }} EUR</p>
      </article>

      <article>
        <h3>Daily rate</h3>
        <p>{{ dailyRate }} EUR</p>
      </article>

      <article>
        <h3>Total payment</h3>
        <p>{{ totalPayment }} EUR</p>
      </article>
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
  </Card>
</template>

<style scoped>
h2,
h3 {
  color: #1f2933;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.message {
  color: #243b53;
  margin-top: 0.5rem;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 0.75rem;
  margin-top: 1rem;
}

.summary-grid article {
  background: #f8fafc;
  border: 1px solid #d8dde3;
  border-radius: 6px;
  padding: 0.75rem;
}

.summary-grid h3 {
  margin: 0;
  font-size: 0.95rem;
}

.summary-grid p {
  margin: 0.4rem 0 0;
  font-size: 1.1rem;
  font-weight: 700;
  color: #334e68;
}

.breakdown-title {
  margin-top: 1.2rem;
}

.table-wrapper {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 0.6rem;
}

th,
td {
  border-bottom: 1px solid #e4e7eb;
  padding: 0.55rem;
  text-align: left;
  color: #334e68;
}

thead th {
  background: #f0f4f8;
  color: #334e68;
}
</style>
