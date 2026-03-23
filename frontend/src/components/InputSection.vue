<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import Card from '@/components/Card.vue';
import { useCreateCalculation } from '@/composables/useBenefitsApi';

const router = useRouter();
const { isLoading, errorMessage, createCalculation } = useCreateCalculation();

const formData = ref({
  grossSalary: null,
  babyBirthDate: '',
});

async function getCalculationResult() {
  if (!formData.value.grossSalary || !formData.value.babyBirthDate) {
    errorMessage.value = 'Please fill in gross salary and baby birth date';

    return;
  }

  const calculationResponse = await createCalculation({
    grossSalary: formData.value.grossSalary,
    babyBirthDate: formData.value.babyBirthDate,
  });

  if (!calculationResponse) {
    return;
  }

  const data = {
    message: calculationResponse.message,
    data: calculationResponse.data,
    errors: calculationResponse.errors,
    grossSalary: formData.value.grossSalary,
    babyBirthDate: formData.value.babyBirthDate,
  };

  await router.push({
    name: 'benefits',
    state: { calculationResult: data },
  });
}
</script>

<template>
  <Card>
    <div class="card-head">
      <span class="icon" aria-hidden="true">◇</span>
      <h2>New calculation</h2>
    </div>

    <form @submit.prevent="getCalculationResult">
      <div class="grid">
        <label>
          <span class="label-text">Gross salary (EUR)</span>
          <input
            v-model.number="formData.grossSalary"
            type="number"
            min="0"
            step="0.01"
            placeholder="3000.00"
          />
        </label>

        <label>
          <span class="label-text">Baby's birth date</span>
          <input v-model="formData.babyBirthDate" type="date" />
        </label>
      </div>

      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

      <div class="actions">
        <button type="submit" :disabled="isLoading">
          {{ isLoading ? 'Calculating…' : 'Calculate benefits' }}
        </button>
      </div>
    </form>
  </Card>
</template>

<style scoped>
.card-head {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.25rem;
}

.icon {
  font-size: 1rem;
  color: var(--c-sage);
  line-height: 1;
}

h2 {
  font-size: 1.05rem;
  font-weight: 600;
  color: var(--c-text);
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 1rem;
}

label {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
}

.label-text {
  font-size: 0.78rem;
  font-weight: 500;
  color: var(--c-text-muted);
}

input[type='number'],
input[type='date'] {
  padding: 0.6rem 0.8rem;
  background: var(--c-warm-gray);
  border: 1.5px solid transparent;
  border-radius: var(--radius-sm);
  color: var(--c-text);
  font-family: var(--font-family);
  font-size: 0.9rem;
  transition: border-color 0.2s, background 0.2s;
  outline: none;
}

input[type='number']:focus,
input[type='date']:focus {
  background: var(--c-white);
  border-color: var(--c-sage);
}

input[type='number']::placeholder {
  color: var(--c-text-light);
}

.actions {
  margin-top: 1.25rem;
}

button[type='submit'] {
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

button[type='submit']:hover:not(:disabled) {
  background: var(--c-sage-dark);
}

button[type='submit']:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  box-shadow: none;
}

.error {
  margin-top: 0.9rem;
  background: var(--c-error-light);
  border: 1px solid #ECC8C8;
  color: var(--c-error);
  font-size: 0.83rem;
  padding: 0.5rem 0.85rem;
  border-radius: var(--radius-sm);
}
</style>
